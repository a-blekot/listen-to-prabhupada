package com.prabhupadalectures.common.filters

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.essenty.lifecycle.LifecycleOwner
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.prabhupadalectures.common.filters.Filters.Model
import com.prabhupadalectures.common.filters.Filters.Output
import com.prabhupadalectures.common.filters.data.QueryParam
import com.prabhupadalectures.common.filters.store.FiltersStore
import com.prabhupadalectures.common.filters.store.FiltersStore.Intent.ClearAll
import com.prabhupadalectures.common.filters.store.FiltersStore.Intent.UpdateFilter
import com.prabhupadalectures.common.filters.store.FiltersStore.State
import com.prabhupadalectures.common.filters.store.FiltersStoreFactory
import com.prabhupadalectures.common.utils.Consumer
import com.prabhupadalectures.common.utils.asValue
import com.prabhupadalectures.common.utils.getStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.coroutines.CoroutineContext


class FiltersComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    private val deps: Dependencies,
    private val output: Consumer<Output>
) : Filters, ComponentContext by componentContext {

    private val store =
        instanceKeeper.getStore {
            FiltersStoreFactory(
                storeFactory = storeFactory,
                deps = deps,
            ).create()
        }

    private val scope: CoroutineScope = lifecycleCoroutineScope(deps.mainContext)

    override val models: Value<Model> = store.asValue().map(stateToModel)

    init {
        store.labels
            .onEach { handleLabel(it) }
            .launchIn(scope)
    }

    override fun onClearAll() = store.accept(ClearAll)
    override fun onQueryParam(queryParam: QueryParam) = store.accept(UpdateFilter(queryParam))
    override fun onApplyChanges() = store.accept(FiltersStore.Intent.ApplyChanges)
    override fun onFilterExpanded(filterName: String, isExpanded: Boolean) =
        deps.db.insertExpandedFilter(filterName, isExpanded)

    private fun handleLabel(label: FiltersStore.Label) {
        when (label) {
            FiltersStore.Label.ApplyChanges -> output(Output.ShowResults)
        }
    }
}

internal val stateToModel: (State) -> Model =
    { state ->
        Model(
            state.isLoading,
            state.filters,
            state.totalLecturesCount,
            state.pagesCount
        )
    }

fun LifecycleOwner.lifecycleCoroutineScope(coroutineContext: CoroutineContext = Dispatchers.Main): CoroutineScope {
    val scope = CoroutineScope(coroutineContext)
    lifecycle.doOnDestroy(scope::cancel)

    return scope
}