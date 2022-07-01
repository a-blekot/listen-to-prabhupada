package com.prabhupadalectures.common.lectures_impl.mvi.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.prabhupadalectures.common.database.Database
import com.prabhupadalectures.common.utils.Lecture
import com.prabhupadalectures.common.lectures_api.LecturesState
import com.prabhupadalectures.common.lectures_impl.data.lectures
import com.prabhupadalectures.common.lectures_impl.data.lectures.dbEntity
import com.prabhupadalectures.common.lectures_impl.data.pagination
import com.prabhupadalectures.common.lectures_impl.mvi.LecturesDeps
import com.prabhupadalectures.common.lectures_impl.mvi.store.LecturesStore.Intent.*
import com.prabhupadalectures.common.lectures_impl.mvi.store.LecturesStore.Label
import com.prabhupadalectures.common.lectures_impl.mvi.store.LecturesStoreFactory.Action.InitialLoad
import com.prabhupadalectures.common.lectures_impl.mvi.store.LecturesStoreFactory.Action.UpdateFromDB
import com.prabhupadalectures.common.network_api.ApiModel
import com.prabhupadalectures.common.network_api.PAGE_QUERY_KEY
import com.prabhupadalectures.common.network_api.QueryParams
import com.prabhupadalectures.common.settings.addPage
import com.prabhupadalectures.common.settings.getFilters
import com.prabhupadalectures.common.settings.settings
import com.prabhupadalectures.common.settings.toDatabaseIdentifier
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
        object UpdateFromDB : Action
        class InitialLoad(val queryParams: QueryParams) : Action
    }

    private sealed interface Msg {
        object StartLoading : Msg
        data class UpdateFromDB(val lectures: List<Lecture>) : Msg
        data class LoadingComplete(val state: LecturesState = LecturesState(isLoading = false)) : Msg
        data class FavoriteChanged(val id: Long, val isFavorite: Boolean) : Msg
        data class CurrentChanged(val id: Long, val isPlaying: Boolean) : Msg
    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
//            scope.launch {
//                dispatch(InitialLoad(settings.getFilters().addPage(deps.db)))
//            }

            deps.db.observeCompleted()
                .onEach { dispatch(UpdateFromDB) }
                .launchIn(scope)

            deps.db.observeAllFavorites()
                .onEach { dispatch(UpdateFromDB) }
                .launchIn(scope)

            deps.db.observeAllDownloads()
                .onEach { dispatch(UpdateFromDB) }
                .launchIn(scope)
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<LecturesStore.Intent, Action, LecturesState, Msg, Label>() {
        override fun executeAction(action: Action, getState: () -> LecturesState) {
            fix when (action) {
                is UpdateFromDB -> {
                    updateFromDB(getState())
                }
//                is InitialLoad -> {
//                    load(action.queryParams)
//                }
            }
        }

        override fun executeIntent(intent: LecturesStore.Intent, getState: () -> LecturesState) {
            if (getState().isLoading) {
                Napier.d("executeIntent canceled, isLoading = true!", tag = "LecturesStoreExecutor")
                return
            }

            when (intent) {
                is CurrentLecture -> setCurrent(intent.id, intent.isPlaying, getState())
                is Favorite -> setFavorite(intent.id, intent.isFavorite, getState())
                is UpdatePage -> load(settings.getFilters().addPage(intent.page))
                is UpdateLectures -> load(settings.getFilters().addPage(deps.db))
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

                        Napier.d("LecturesStore LecturesLoaded -> ${newState.lectures.map {it.id}}", tag = "LECTURES")
                        dispatch(Msg.LoadingComplete(newState))
                        publish(Label.LecturesLoaded(newState.lectures))
                    }
                } else {
                    Napier.e(message = "api.getResults isFailure ${result.exceptionOrNull()?.message}", throwable = result.exceptionOrNull(), tag = "LecturesStore")
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

        private fun updatePlaylist(lectures: List<Lecture>) =
            scope.launch {
                publish(Label.UpdatePlaylist(lectures))
            }

        private fun updateFromDB(state: LecturesState) =
            scope.launch {
                dispatch(Msg.UpdateFromDB(lectures = state.lectures.updateFromDB()))
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
                is Msg.UpdateFromDB -> copy(lectures = msg.lectures)
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
        put(PAGE_QUERY_KEY, db.selectPage(toDatabaseIdentifier()))
    }

//    private fun share(lectureId: Long) {
//        val queryParams = resultsRepository.queryParams()
//        val timeMs = playbackRepository.currentState().run {
//            if (lectureId == lecture.id) timeMs else null
//        }
//
//        ShareAction(lectureId, queryParams, timeMs).let {
//            setEffect { ResultsEffect.Share(it) }
//        }
//    }
//}

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