package com.prabhupadalectures.common.favorites_impl.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.prabhupadalectures.common.favorites_impl.FavoritesDeps
import com.prabhupadalectures.common.favorites_api.FavoritesState
import com.prabhupadalectures.common.favorites_impl.dbEntity
import com.prabhupadalectures.common.favorites_impl.mapped
import com.prabhupadalectures.common.favorites_impl.store.FavoritesIntent.CurrentLecture
import com.prabhupadalectures.common.favorites_impl.store.FavoritesIntent.Favorite
import com.prabhupadalectures.common.favorites_impl.store.FavoritesStoreFactory.Action.InitialLoad
import com.prabhupadalectures.common.favorites_impl.store.FavoritesStoreFactory.Action.UpdateFromDB
import com.prabhupadalectures.common.utils.Lecture
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class FavoritesStoreFactory(
    private val storeFactory: StoreFactory,
    private val deps: FavoritesDeps
) {

    fun create(): FavoritesStore =
        object : FavoritesStore, Store<FavoritesIntent, FavoritesState, FavoritesLabel> by storeFactory.create(
            name = "FavoritesStore",
            autoInit = false,
            initialState = FavoritesState(),
            bootstrapper = BootstrapperImpl(),
            executorFactory = { ExecutorImpl() },
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
        object InitialLoad : Action
        object UpdateFromDB : Action
    }

    private sealed interface Msg {
        data class UpdateFromDB(val lectures: List<Lecture>) : Msg
        data class LoadingComplete(val state: FavoritesState = FavoritesState()) : Msg
        data class FavoriteChanged(val id: Long, val isFavorite: Boolean) : Msg
        data class CurrentChanged(val id: Long, val isPlaying: Boolean) : Msg
    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                dispatch(InitialLoad)
            }

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

    private inner class ExecutorImpl : CoroutineExecutor<FavoritesIntent, Action, FavoritesState, Msg, FavoritesLabel>() {
        override fun executeAction(action: Action, getState: () -> FavoritesState) {
            when (action) {
                is UpdateFromDB -> {
                    updateFromDB(getState())
                }
                is InitialLoad -> {
                    val lectures = deps.db.selectAllFavorites().mapped()
                    dispatch(Msg.LoadingComplete(FavoritesState(lectures)))

                    scope.launch {
                        publish(FavoritesLabel.LecturesLoaded(lectures))
                    }
                }
            }
        }

        override fun executeIntent(intent: FavoritesIntent, getState: () -> FavoritesState) {
            when (intent) {
                is CurrentLecture -> setCurrent(intent.id, intent.isPlaying, getState())
                is Favorite -> setFavorite(intent.id, intent.isFavorite, getState())
            }
        }

        private fun updateFromDB(state: FavoritesState) =
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

        private fun setCurrent(id: Long, isPlaying: Boolean, state: FavoritesState) {
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

        private fun setFavorite(id: Long, isFavorite: Boolean, state: FavoritesState) {
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

    private object ReducerImpl : Reducer<FavoritesState, Msg> {
        override fun FavoritesState.reduce(msg: Msg): FavoritesState =
            when (msg) {
                is Msg.FavoriteChanged -> update(id = msg.id) { copy(isFavorite = msg.isFavorite) }
                is Msg.UpdateFromDB -> copy(lectures = msg.lectures)
                is Msg.CurrentChanged -> copy(lectures = lectures.map { it.copy(isPlaying = it.id == msg.id && msg.isPlaying) })
                is Msg.LoadingComplete -> msg.state
            }

        private inline fun FavoritesState.update(id: Long, func: Lecture.() -> Lecture): FavoritesState {
            val lecture = lectures.find { it.id == id } ?: return this
            return put(lecture.func())
        }

        private fun FavoritesState.put(lecture: Lecture): FavoritesState {
            val oldItems = lectures.associateByTo(mutableMapOf(), Lecture::id)
            oldItems[lecture.id] = lecture

            return copy(lectures = oldItems.values.toList())
        }
    }
}
