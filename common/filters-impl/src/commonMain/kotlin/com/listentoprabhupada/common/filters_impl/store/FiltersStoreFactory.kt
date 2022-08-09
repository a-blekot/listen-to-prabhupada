package com.listentoprabhupada.common.filters_impl.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.listentoprabhupada.common.filters_impl.FiltersDeps
import com.listentoprabhupada.common.filters_impl.data.*
import com.listentoprabhupada.common.filters_impl.store.FiltersStoreFactory.Action.InitialLoad
import com.listentoprabhupada.common.filters_impl.store.FiltersStoreFactory.Action.NeedInitialLoad
import com.listentoprabhupada.common.filters_api.Filter
import com.listentoprabhupada.common.filters_api.FiltersState
import com.listentoprabhupada.common.filters_api.decodeFiltersList
import com.listentoprabhupada.common.filters_api.encodeToString
import com.listentoprabhupada.common.network_api.ApiModel
import com.listentoprabhupada.common.network_api.QueryParams
import com.listentoprabhupada.common.settings.*
import io.github.aakira.napier.Napier
import kotlinx.coroutines.launch

internal class FiltersStoreFactory(
    private val storeFactory: StoreFactory,
    private val deps: FiltersDeps,
) {

    fun create(): FiltersStore =
        object : FiltersStore, Store<FiltersIntent, FiltersState, FiltersLabel> by storeFactory.create(
            name = "FiltersStore",
            initialState = FiltersState(),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
        object NeedInitialLoad: Action
        data class InitialLoad(val state: FiltersState) : Action
    }

    private sealed interface Msg {
        object StartLoading : Msg
        data class LoadingComplete(val state: FiltersState = FiltersState(isLoading = false)) : Msg
    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
               val state = // withContext(deps.dispatchers.io) {
                   FiltersState(
                       filters = settings.getFilterOptions().decodeFiltersList().updateFromDB(),
                       totalLecturesCount = settings.getTotalLecturesCount(),
                       pagesCount = settings.getPagesCount(),
                   )
                // }

                if (state.filters.isNotEmpty()) {
                    dispatch(InitialLoad(state))
                } else {
                    dispatch(NeedInitialLoad)
                }
            }
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<FiltersIntent, Action, FiltersState, Msg, FiltersLabel>() {
        override fun executeAction(action: Action, getState: () -> FiltersState) =
            when (action) {
                is NeedInitialLoad -> load(buildQueryParams())
                is InitialLoad -> dispatch(Msg.LoadingComplete(action.state))
            }

        override fun executeIntent(intent: FiltersIntent, getState: () -> FiltersState) {
            if (getState().isLoading) {
                Napier.d("executeIntent canceled, isLoading = true!", tag = "FiltersStoreExecutor")
                return
            }

            when (intent) {
                is FiltersIntent.ClearAll -> load(buildQueryParams())
                is FiltersIntent.UpdateFilter -> load(buildQueryParams(getState().filters, intent.queryParam))
                is FiltersIntent.ApplyChanges -> applyChanges(getState())
            }
        }

        private fun load(queryParams: QueryParams) {
            scope.launch {
                dispatch(Msg.StartLoading)

                val result = deps.api.getResults(queryParams)
                if (result.isSuccess) {
                    result.getOrNull()?.let { apiModel ->
                        dispatch(Msg.LoadingComplete(state(apiModel)))
                    }
                } else {
                    Napier.e(message = "api.getResults isFailure", throwable = result.exceptionOrNull())
                    dispatch(Msg.LoadingComplete())
                }
            }
        }

        private fun state(apiModel: ApiModel) =
            FiltersState(
                isLoading = false,
                filters = filters(apiModel).updateFromDB(),
                totalLecturesCount = totalLecturesCount(apiModel),
                pagesCount = pagesCount(apiModel),
            )

        private fun applyChanges(state: FiltersState) {
            settings.run {
                state.run {
                    saveFilterOptions(filters.encodeToString())
                    saveFilters(buildQueryParams(filters))
                    saveTotalLecturesCount(totalLecturesCount)
                    savePagesCount(pagesCount)
                }
            }
            publish(FiltersLabel.ApplyChanges)
        }
    }

    private fun List<Filter>.updateFromDB() =
        map { filter ->
            filter.copy(isExpanded = deps.db.selectExpandedFilter(filter.name))
        }

    private object ReducerImpl : Reducer<FiltersState, Msg> {
        override fun FiltersState.reduce(msg: Msg): FiltersState =
            when (msg) {
                Msg.StartLoading -> copy(isLoading = true)
                is Msg.LoadingComplete -> msg.state
            }
    }
}