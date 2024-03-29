package com.listentoprabhupada.common.downloads_impl.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.listentoprabhupada.common.downloads_impl.DownloadsDeps
import com.listentoprabhupada.common.downloads_api.DownloadsState
import com.listentoprabhupada.common.utils.dbEntity
import com.listentoprabhupada.common.utils.mapped
import com.listentoprabhupada.common.downloads_impl.store.DownloadsIntent.CurrentLecture
import com.listentoprabhupada.common.downloads_impl.store.DownloadsIntent.Favorite
import com.listentoprabhupada.common.data.Lecture
import com.listentoprabhupada.common.downloads_impl.store.DownloadsStoreFactory.Action.*
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class DownloadsStoreFactory(
    private val storeFactory: StoreFactory,
    private val deps: DownloadsDeps
) {

    fun create(): DownloadsStore =
        object : DownloadsStore, Store<DownloadsIntent, DownloadsState, DownloadsLabel> by storeFactory.create(
            name = "DownloadsStore",
            autoInit = false,
            initialState = DownloadsState(),
            bootstrapper = BootstrapperImpl(),
            executorFactory = { ExecutorImpl() },
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
        object RefreshDownloads : Action
        object UpdateFromDB : Action
        object UpdateCompleted : Action
    }

    private sealed interface Msg {
        data class UpdateFromDB(val lectures: List<Lecture>) : Msg
        data class LoadingComplete(val state: DownloadsState = DownloadsState()) : Msg
        data class FavoriteChanged(val id: Long, val isFavorite: Boolean) : Msg
        data class CurrentChanged(val id: Long, val isPlaying: Boolean) : Msg
    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                dispatch(RefreshDownloads)
            }

            deps.db.observeCompleted()
                .onEach { dispatch(UpdateCompleted) }
                .launchIn(scope)

            deps.db.observeAllFavorites()
                .onEach { dispatch(UpdateFromDB) }
                .launchIn(scope)

            deps.db.observeAllDownloads()
                .onEach {
                    Napier.d("observeAllDownloads")
                    dispatch(RefreshDownloads)
                }
                .launchIn(scope)
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<DownloadsIntent, Action, DownloadsState, Msg, DownloadsLabel>() {
        override fun executeAction(action: Action, getState: () -> DownloadsState) {
            when (action) {
                is UpdateFromDB -> updateFromDB(getState())
                is UpdateCompleted -> updateCompleted(getState())
                is RefreshDownloads -> {
                    val lectures = deps.db.selectAllDownloads().mapped()
                    dispatch(Msg.LoadingComplete(DownloadsState(lectures)))

                    scope.launch {
                        publish(DownloadsLabel.LecturesLoaded(lectures))
                    }
                }
            }
        }

        override fun executeIntent(intent: DownloadsIntent, getState: () -> DownloadsState) {
            when (intent) {
                is CurrentLecture -> setCurrent(intent.id, intent.isPlaying, getState())
                is Favorite -> setFavorite(intent.id, intent.isFavorite, getState())
            }
        }

        private fun updateFromDB(state: DownloadsState) =
            scope.launch {
                dispatch(Msg.UpdateFromDB(lectures = state.lectures.updateFromDB()))
            }

        private fun updateCompleted(state: DownloadsState) =
            scope.launch {
                dispatch(Msg.UpdateFromDB(lectures = state.lectures.updateCompleted()))
            }

        private fun List<Lecture>.updateFromDB() =
            map { lecture ->
                val lectureEntity = deps.db.selectLecture(lecture.id)
                val isCompleted = deps.db.selectCompleted(lecture.id)
                lecture.copy(
                    fileUrl = lectureEntity?.fileUrl ?: lecture.fileUrl,
                    isFavorite = lectureEntity?.isFavorite ?: lecture.isFavorite,
                    isCompleted = isCompleted ?: lecture.isCompleted,
                    downloadProgress = lectureEntity?.downloadProgress?.toInt() ?: lecture.downloadProgress
                )
            }

        private fun List<Lecture>.updateCompleted() =
            map { it.copy(isCompleted = deps.db.selectCompleted(it.id) ?: it.isCompleted) }

        private fun setCurrent(id: Long, isPlaying: Boolean, state: DownloadsState) {
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

        private fun setFavorite(id: Long, isFavorite: Boolean, state: DownloadsState) {
            scope.launch {
                withContext(deps.dispatchers.io) {
                    state.lectures.find { it.id == id }?.let {
                        deps.db.insertLecture(it.copy(isFavorite = isFavorite).dbEntity())
                    }
                }
                dispatch(Msg.FavoriteChanged(id = id, isFavorite = isFavorite))
            }
        }
    }

    private object ReducerImpl : Reducer<DownloadsState, Msg> {
        override fun DownloadsState.reduce(msg: Msg): DownloadsState =
            when (msg) {
                is Msg.FavoriteChanged -> update(id = msg.id) { copy(isFavorite = msg.isFavorite) }
                is Msg.UpdateFromDB -> copy(lectures = msg.lectures)
                is Msg.CurrentChanged -> copy(lectures = lectures.map { it.copy(isPlaying = it.id == msg.id && msg.isPlaying) })
                is Msg.LoadingComplete -> msg.state
            }

        private inline fun DownloadsState.update(id: Long, func: Lecture.() -> Lecture): DownloadsState {
            val lecture = lectures.find { it.id == id } ?: return this
            return put(lecture.func())
        }

        private fun DownloadsState.put(lecture: Lecture): DownloadsState {
            val oldItems = lectures.associateByTo(mutableMapOf(), Lecture::id)
            oldItems[lecture.id] = lecture

            return copy(lectures = oldItems.values.toList())
        }
    }
}
