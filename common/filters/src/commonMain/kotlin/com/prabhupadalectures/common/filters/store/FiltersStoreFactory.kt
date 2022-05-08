package com.prabhupadalectures.common.filters.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.prabhupadalectures.common.filters.Dependencies
import com.prabhupadalectures.common.filters.data.*
import com.prabhupadalectures.common.filters.store.FiltersStore.*
import com.prabhupadalectures.common.filters.store.FiltersStoreFactory.Action.InitialLoad
import com.prabhupadalectures.common.filters.store.FiltersStoreFactory.Action.NeedInitialLoad
import com.prabhupadalectures.common.network_api.ApiModel
import com.prabhupadalectures.common.network_api.QueryParams
import com.prabhupadalectures.common.settings.*
import io.github.aakira.napier.Napier
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class FiltersStoreFactory(
    private val storeFactory: StoreFactory,
    private val deps: Dependencies,
) {

    fun create(): FiltersStore =
        object : FiltersStore, Store<Intent, State, Label> by storeFactory.create(
            name = "FiltersStore",
            initialState = State(),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
        object NeedInitialLoad: Action
        data class InitialLoad(val state: State) : Action
    }

    private sealed interface Msg {
        object StartLoading : Msg
        data class LoadingComplete(val state: State = State(isLoading = false)) : Msg
    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
               val state = withContext(deps.ioContext) {
                   State(
                       filters = settings.getFilterOptions().decodeFiltersList().updateFromDB(),
                       totalLecturesCount = settings.getTotalLecturesCount(),
                       pagesCount = settings.getPagesCount(),
                   )
                }

                if (state.filters.isNotEmpty()) {
                    dispatch(InitialLoad(state))
                } else {
                    dispatch(NeedInitialLoad)
                }
            }
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeAction(action: Action, getState: () -> State) =
            when (action) {
                is NeedInitialLoad -> load(buildQueryParams())
                is InitialLoad -> dispatch(Msg.LoadingComplete(action.state))
            }

        override fun executeIntent(intent: Intent, getState: () -> State) {
            if (getState().isLoading) {
                Napier.d("executeIntent canceled, isLoading = true!", tag = "FiltersStoreExecutor")
                return
            }

            when (intent) {
                is Intent.ClearAll -> load(buildQueryParams())
                is Intent.UpdateFilter -> load(buildQueryParams(getState().filters, intent.queryParam))
                is Intent.ApplyChanges -> applyChanges(getState())
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
            State(
                isLoading = false,
                filters = filters(apiModel).updateFromDB(),
                totalLecturesCount = totalLecturesCount(apiModel),
                pagesCount = pagesCount(apiModel),
            )

        private fun applyChanges(state: State) {
            settings.run {
                state.run {
                    saveFilterOptions(filters.encodeToString())
                    saveFilters(buildQueryParams(filters))
                    saveTotalLecturesCount(totalLecturesCount)
                    savePagesCount(pagesCount)
                }
            }
            publish(Label.ApplyChanges)
        }
    }

    private fun List<Filter>.updateFromDB() =
        map { filter ->
            filter.copy(isExpanded = deps.db.selectExpandedFilter(filter.name))
        }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                Msg.StartLoading -> copy(isLoading = true)
                is Msg.LoadingComplete -> msg.state
            }
    }
}