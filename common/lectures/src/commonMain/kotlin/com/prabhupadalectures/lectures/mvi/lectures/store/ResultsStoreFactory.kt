package com.prabhupadalectures.lectures.mvi.lectures.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.prabhupadalectures.common.network_api.ApiModel
import com.prabhupadalectures.common.network_api.QueryParams
import com.prabhupadalectures.lectures.data.ApiMapper
import com.prabhupadalectures.lectures.data.Pagination
import com.prabhupadalectures.lectures.data.QueryParam
import com.prabhupadalectures.lectures.data.buildQueryParams
import com.prabhupadalectures.lectures.data.filters.Filter
import com.prabhupadalectures.lectures.data.lectures.Lecture
import com.prabhupadalectures.lectures.mvi.Dependencies
import com.prabhupadalectures.lectures.mvi.lectures.store.ResultsStore.Intent.*
import com.prabhupadalectures.lectures.mvi.lectures.store.ResultsStore.State
import com.prabhupadalectures.lectures.mvi.lectures.store.ResultsStoreFactory.Action.InitialLoad
import com.prabhupadalectures.lectures.repository.*
import io.github.aakira.napier.Napier
import kotlinx.coroutines.launch

internal class ResultsStoreFactory(
    private val storeFactory: StoreFactory,
    private val deps: Dependencies,
) {

    fun create(): ResultsStore =
        object : ResultsStore, Store<ResultsStore.Intent, State, Nothing> by storeFactory.create(
            name = "ResultsStore",
            initialState = State(),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
        class InitialLoad(val queryParams: QueryParams) : Action
    }

    private sealed interface Msg {
        object StartLoading : Msg
        data class LoadingComplete(val state: State = State(isLoading = false)) : Msg
        data class FavoriteChanged(val id: Long, val isFavorite: Boolean) : Msg
    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                val queryParams =
                    settings
                        .getQueryParams()
                        .addPage(settings.getPage())

                dispatch(InitialLoad(queryParams))
            }
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<ResultsStore.Intent, Action, State, Msg, Nothing>() {
        override fun executeAction(action: Action, getState: () -> State) =
            when (action) {
                is InitialLoad -> load(action.queryParams, getState())
            }

        override fun executeIntent(intent: ResultsStore.Intent, getState: () -> State) {
            val state = getState()

            when (intent) {
//                is Intent.LoadPrev -> setItemDone(id = intent.id, isDone = intent.isDone)
//                is Intent.LoadNext -> deleteItem(id = intent.id)
//                is Intent.Player -> dispatch(Msg.TextChanged(text = intent.text))
                is Favorite -> setFavorite(id = intent.id, isFavorite = intent.isFavorite)

                is UpdatePage -> load(buildQueryParams(state, intent.page), state)
                is UpdateQuery -> load(buildQueryParams(state, intent.queryParam), state)
                is ClearAllFilters -> load(emptyQueryParams(), state)
                else -> {
                    /** do nothing **/
                }
            }
        }


        private fun load(queryParams: QueryParams, state: State) {
            scope.launch {
                if (state.isLoading) {
                    Napier.d("loadMore canceled, isLoading = true!", tag = "ResultsRepository")
                    return@launch
                }

                dispatch(Msg.StartLoading)

                val result = deps.api.getResults(queryParams)
                if (result.isSuccess) {
                    result.getOrNull()?.let { apiModel ->
                        dispatch(Msg.LoadingComplete(newState(apiModel)))
                    }
                } else {
                    Napier.e(message = "api.getResults isFailure", throwable = result.exceptionOrNull())
                    dispatch(Msg.LoadingComplete())
                }
            }
        }

        private fun setFavorite(id: Long, isFavorite: Boolean) {
            scope.launch {
//                deps.db.insertLecture(lecture.copy(isFavorite = isFavorite))
                dispatch(Msg.FavoriteChanged(id = id, isFavorite = isFavorite))
            }
        }

        private fun newState(apiModel: ApiModel): State {
            val state = State(
                isLoading = false,
                lectures = ApiMapper.lectures(apiModel).updateLecturesFromDB(),
                filters = ApiMapper.filters(apiModel).updateFiltersFromDB(),
                pagination = Pagination(apiModel),
            )

            val queryParams = buildQueryParams(state).toQueryParamsStringWithoutPage()

            settings.saveQueryParams(queryParams)
            settings.savePage(state.pagination.curr)
            deps.db.insertPage(queryParams.hashCode().toLong(), state.pagination.curr)

            return state
        }

        private fun List<Lecture>.updateLecturesFromDB() =
            map { lecture ->
                val lectureEntity = deps.db.selectLecture(lecture.id)
                lecture.copy(
                    fileUrl = lectureEntity?.fileUrl ?: lecture.fileUrl,
                    isFavorite = lectureEntity?.isFavorite ?: lecture.isFavorite,
                    isCompleted = lectureEntity?.isCompleted ?: lecture.isCompleted,
                    downloadProgress = lectureEntity?.downloadProgress?.toInt() ?: lecture.downloadProgress
                )
            }

        private fun List<Filter>.updateFiltersFromDB() =
            map { filter ->
                filter.copy(isExpanded = deps.db.selectExpandedFilter(filter.name))
            }

        private fun buildQueryParams(state: State, queryParam: QueryParam? = null) =
            buildQueryParams(deps.db, state.filters, state.pagination.curr, queryParam)

        private fun buildQueryParams(state: State, page: Int) =
            buildQueryParams(deps.db, state.filters, page)

        private fun emptyQueryParams() =
            buildQueryParams(deps.db, queryParam = QueryParam())
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                Msg.StartLoading -> copy(isLoading = true)
                is Msg.FavoriteChanged -> update(id = msg.id) { copy(isFavorite = msg.isFavorite) }
                is Msg.LoadingComplete -> msg.state
            }

        private inline fun State.update(id: Long, func: Lecture.() -> Lecture): State {
            val lecture = lectures.find { it.id == id } ?: return this
            return put(lecture.func())
        }

        private fun State.put(lecture: Lecture): State {
            val oldItems = lectures.associateByTo(mutableMapOf(), Lecture::id)
            oldItems[lecture.id] = lecture

            return copy(lectures = oldItems.values.toList())
        }
    }
}