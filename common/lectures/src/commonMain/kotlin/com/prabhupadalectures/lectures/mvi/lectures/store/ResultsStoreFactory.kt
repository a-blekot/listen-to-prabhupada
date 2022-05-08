package com.prabhupadalectures.lectures.mvi.lectures.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.prabhupadalectures.common.database.Database
import com.prabhupadalectures.common.network_api.ApiModel
import com.prabhupadalectures.common.network_api.QueryParams
import com.prabhupadalectures.common.settings.addPage
import com.prabhupadalectures.common.settings.getFilters
import com.prabhupadalectures.common.settings.settings
import com.prabhupadalectures.common.settings.toDatabaseIdentifier
import com.prabhupadalectures.lectures.data.Pagination
import com.prabhupadalectures.lectures.data.lectures
import com.prabhupadalectures.lectures.data.lectures.Lecture
import com.prabhupadalectures.lectures.mvi.Dependencies
import com.prabhupadalectures.lectures.mvi.lectures.store.ResultsStore.Intent.*
import com.prabhupadalectures.lectures.mvi.lectures.store.ResultsStore.State
import com.prabhupadalectures.lectures.mvi.lectures.store.ResultsStoreFactory.Action.InitialLoad
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

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                dispatch(InitialLoad(settings.getFilters().addPage(deps.db)))
            }
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<ResultsStore.Intent, Action, State, Msg, Nothing>() {
        override fun executeAction(action: Action, getState: () -> State) =
            when (action) {
                is InitialLoad -> load(action.queryParams)
            }

        override fun executeIntent(intent: ResultsStore.Intent, getState: () -> State) {
            if (getState().isLoading) {
                Napier.d("executeIntent canceled, isLoading = true!", tag = "ResultsStoreExecutor")
                return
            }

            when (intent) {
//                is Intent.LoadPrev -> setItemDone(id = intent.id, isDone = intent.isDone)
//                is Intent.LoadNext -> deleteItem(id = intent.id)
//                is Intent.Player -> dispatch(Msg.TextChanged(text = intent.text))
                is Favorite -> setFavorite(id = intent.id, isFavorite = intent.isFavorite)
                is UpdatePage -> load(settings.getFilters().addPage(intent.page))
                is UpdateFilters -> load(settings.getFilters().addPage(deps.db))
                else -> {
                    /** do nothing **/
                }
            }
        }

        private fun load(queryParams: QueryParams) {
            scope.launch {
                dispatch(Msg.StartLoading)

                val result = deps.api.getResults(queryParams)
                if (result.isSuccess) {
                    result.getOrNull()?.let { apiModel ->

                        val newState = state(apiModel)
                        deps.db.insertPage(settings.getFilters().toDatabaseIdentifier(), newState.pagination.curr)

                        dispatch(Msg.LoadingComplete(newState))
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

        private fun state(apiModel: ApiModel) =
            State(
                isLoading = false,
                lectures = lectures(apiModel).updateFromDB(),
                pagination = Pagination(apiModel),
            )

        private fun List<Lecture>.updateFromDB() =
            map { lecture ->
                val lectureEntity = deps.db.selectLecture(lecture.id)
                lecture.copy(
                    fileUrl = lectureEntity?.fileUrl ?: lecture.fileUrl,
                    isFavorite = lectureEntity?.isFavorite ?: lecture.isFavorite,
                    isCompleted = lectureEntity?.isCompleted ?: lecture.isCompleted,
                    downloadProgress = lectureEntity?.downloadProgress?.toInt() ?: lecture.downloadProgress
                )
            }
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

fun HashMap<String, Any>.addPage(db: Database) =
    apply {
        put(com.prabhupadalectures.common.network_api.PAGE_QUERY_KEY, db.selectPage(toDatabaseIdentifier()))
    }

//
//private fun observeDownloads() =
//    launch {
//        db.observeAllDownloads().collect {
//            updateFromDb()
//        }
//    }
//
//private fun observeFavorites() =
//    launch {
//        db.observeAllFavorites().collect {
//            updateFromDb()
//        }
//    }
//
//private fun observeCompleted() =
//    launch {
//        db.observeCompleted().collect {
//            updateFromDb()
//        }
//    }
//
//private fun observeSelfState() =
//    launch {
//        observeState().collect {
//            playbackRepository.updatePlaylist(it.lectures)
//            shareAction?.run {
//                playbackRepository.run {
//                    delay(200L)
//                    handleAction(Play(lectureId))
//                    if (timeMs != null) {
//                        delay(200L)
//                        handleAction(SeekTo(timeMs))
//                        handleAction(SliderReleased)
//                    }
//                }
//            }
//            shareAction = null
//        }
//    }