package com.prabhupadalectures.common.filters

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.prabhupadalectures.common.filters.FiltersComponent.Output
import com.prabhupadalectures.common.filters.data.QueryParam
import com.prabhupadalectures.common.filters.store.FiltersStore
import com.prabhupadalectures.common.filters.store.FiltersStore.Intent.ClearAll
import com.prabhupadalectures.common.filters.store.FiltersStore.Intent.UpdateFilter
import com.prabhupadalectures.common.filters.store.FiltersStoreFactory
import com.prabhupadalectures.common.utils.Consumer
import com.prabhupadalectures.common.utils.asValue
import com.prabhupadalectures.common.utils.getStore
import com.prabhupadalectures.common.utils.lifecycleCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.coroutines.CoroutineContext


class FiltersComponentImpl(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    private val deps: FiltersDeps,
    private val output: Consumer<Output>
) : FiltersComponent, ComponentContext by componentContext {

    private val store =
        instanceKeeper.getStore {
            FiltersStoreFactory(
                storeFactory = storeFactory,
                deps = deps,
            ).create()
        }

    private val scope: CoroutineScope = lifecycleCoroutineScope(deps.dispatchers.main)

    override val models: Value<FiltersState> = store.asValue()

    init {
        store.labels
            .onEach(::handleLabel)
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
