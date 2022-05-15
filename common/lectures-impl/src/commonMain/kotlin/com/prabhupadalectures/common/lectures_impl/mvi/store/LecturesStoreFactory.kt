package com.prabhupadalectures.common.lectures_impl.mvi.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.prabhupadalectures.common.database.Database
import com.prabhupadalectures.common.lectures_api.Lecture
import com.prabhupadalectures.common.lectures_api.LecturesState
import com.prabhupadalectures.common.lectures_impl.data.lectures
import com.prabhupadalectures.common.lectures_impl.data.lectures.dbEntity
import com.prabhupadalectures.common.lectures_impl.data.pagination
import com.prabhupadalectures.common.lectures_impl.mvi.LecturesDeps
import com.prabhupadalectures.common.lectures_impl.mvi.store.LecturesStore.Intent.*
import com.prabhupadalectures.common.lectures_impl.mvi.store.LecturesStore.Label
import com.prabhupadalectures.common.lectures_impl.mvi.store.LecturesStoreFactory.Action.InitialLoad
import com.prabhupadalectures.common.network_api.ApiModel
import com.prabhupadalectures.common.network_api.QueryParams
import com.prabhupadalectures.common.settings.addPage
import com.prabhupadalectures.common.settings.getFilters
import com.prabhupadalectures.common.settings.settings
import com.prabhupadalectures.common.settings.toDatabaseIdentifier
import io.github.aakira.napier.Napier
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class LecturesStoreFactory(
    private val storeFactory: StoreFactory,
    private val deps: LecturesDeps,
) {

    fun create(): LecturesStore =
        object : LecturesStore, Store<LecturesStore.Intent, LecturesState, Label> by storeFactory.create(
            name = "ResultsStore",
            initialState = LecturesState(),
            bootstrapper = BootstrapperImpl(),
            executorFactory = { ExecutorImpl() },
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
        class InitialLoad(val queryParams: QueryParams) : Action
    }

    private sealed interface Msg {
        object StartLoading : Msg
        data class LoadingComplete(val state: LecturesState = LecturesState(isLoading = false)) : Msg
        data class FavoriteChanged(val id: Long, val isFavorite: Boolean) : Msg
        data class CurrentChanged(val id: Long, val isPlaying: Boolean) : Msg
    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                dispatch(InitialLoad(settings.getFilters().addPage(deps.db)))
            }
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<LecturesStore.Intent, Action, LecturesState, Msg, Label>() {
        override fun executeAction(action: Action, getState: () -> LecturesState) =
            when (action) {
                is InitialLoad -> load(action.queryParams)
            }

        override fun executeIntent(intent: LecturesStore.Intent, getState: () -> LecturesState) {
            if (getState().isLoading) {
                Napier.d("executeIntent canceled, isLoading = true!", tag = "ResultsStoreExecutor")
                return
            }

            when (intent) {
                is CurrentLecture -> setCurrent(intent.id, intent.isPlaying, getState())
                is Favorite -> setFavorite(id = intent.id, isFavorite = intent.isFavorite, getState())
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
                        publish(Label.LecturesLoaded(newState.lectures))
                    }
                } else {
                    Napier.e(message = "api.getResults isFailure", throwable = result.exceptionOrNull())
                    dispatch(Msg.LoadingComplete())
                }
            }
        }

        private fun setFavorite(id: Long, isFavorite: Boolean, state: LecturesState) {
            scope.launch {
                withContext(deps.dispatchers.io) {
                    state.lectures.find { it.id == id }?.let {
                        deps.db.insertLecture(it.copy(isFavorite = isFavorite).dbEntity())
                    }
                }
                dispatch(Msg.FavoriteChanged(id = id, isFavorite = isFavorite))
            }
        }

        private fun state(apiModel: ApiModel) =
            LecturesState(
                isLoading = false,
                lectures = lectures(apiModel).updateFromDB(),
                pagination = pagination(apiModel),
            )

        private fun setCurrent(id: Long, isPlaying: Boolean, state: LecturesState) {
            state.lectures
                .firstOrNull { it.id == id }
                ?.run {
                    if (this.isPlaying != isPlaying) {
                        scope.launch {
                            dispatch(Msg.CurrentChanged(id = id, isPlaying = isPlaying))
                        }
                    }
                }
        }

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

    private object ReducerImpl : Reducer<LecturesState, Msg> {
        override fun LecturesState.reduce(msg: Msg): LecturesState =
            when (msg) {
                Msg.StartLoading -> copy(isLoading = true)
                is Msg.FavoriteChanged -> update(id = msg.id) { copy(isFavorite = msg.isFavorite) }
                is Msg.CurrentChanged -> copy(lectures = lectures.map { it.copy(isPlaying = it.id == msg.id && msg.isPlaying) })
                is Msg.LoadingComplete -> msg.state
            }

        private inline fun LecturesState.update(id: Long, func: Lecture.() -> Lecture): LecturesState {
            val lecture = lectures.find { it.id == id } ?: return this
            return put(lecture.func())
        }

        private fun LecturesState.put(lecture: Lecture): LecturesState {
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