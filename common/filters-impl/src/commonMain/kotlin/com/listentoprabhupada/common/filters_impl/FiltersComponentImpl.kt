package com.listentoprabhupada.common.filters_impl

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.listentoprabhupada.common.filters_api.FiltersComponent
import com.listentoprabhupada.common.filters_api.FiltersOutput
import com.listentoprabhupada.common.filters_api.FiltersState
import com.listentoprabhupada.common.filters_api.QueryParam
import com.listentoprabhupada.common.filters_impl.store.FiltersIntent
import com.listentoprabhupada.common.filters_impl.store.FiltersIntent.ClearAll
import com.listentoprabhupada.common.filters_impl.store.FiltersIntent.UpdateFilter
import com.listentoprabhupada.common.filters_impl.store.FiltersLabel
import com.listentoprabhupada.common.filters_impl.store.FiltersStoreFactory
import com.listentoprabhupada.common.utils.Consumer
import com.listentoprabhupada.common.utils.asValue
import com.listentoprabhupada.common.utils.getStore
import com.listentoprabhupada.common.utils.lifecycleCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


class FiltersComponentImpl(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    private val deps: FiltersDeps,
    private val output: Consumer<FiltersOutput>
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
    override fun onApplyChanges() = store.accept(FiltersIntent.ApplyChanges)
    override fun onFilterExpanded(filterName: String, isExpanded: Boolean) =
        deps.db.insertExpandedFilter(filterName, isExpanded)

    private fun handleLabel(label: FiltersLabel) {
        when (label) {
            FiltersLabel.ApplyChanges -> output(FiltersOutput.ShowResults)
        }
    }
}
