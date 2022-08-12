package com.listentoprabhupada.common.results_impl.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.listentoprabhupada.common.database.Database
import com.listentoprabhupada.common.data.Lecture
import com.listentoprabhupada.common.results_api.ResultsState
import com.listentoprabhupada.common.results_impl.data.lectures
import com.listentoprabhupada.common.utils.dbEntity
import com.listentoprabhupada.common.results_impl.data.pagination
import com.listentoprabhupada.common.results_impl.ResultsDeps
import com.listentoprabhupada.common.results_impl.store.ResultsIntent.*
import com.listentoprabhupada.common.results_impl.store.ResultsStoreFactory.Action.UpdateFromDB
import com.listentoprabhupada.common.network_api.ApiModel
import com.listentoprabhupada.common.network_api.PAGE_QUERY_KEY
import com.listentoprabhupada.common.network_api.QueryParams
import com.listentoprabhupada.common.results_impl.store.ResultsStoreFactory.Action.UpdateCompleted
import com.listentoprabhupada.common.settings.addPage
import com.listentoprabhupada.common.settings.getFilters
import com.listentoprabhupada.common.settings.settings
import com.listentoprabhupada.common.settings.toDatabaseIdentifier
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class ResultsStoreFactory(
    private val storeFactory: StoreFactory,
    private val deps: ResultsDeps,
) {

    fun create(): ResultsStore =
        object : ResultsStore, Store<ResultsIntent, ResultsState, ResultsLabel> by storeFactory.create(
            name = "ResultsStore",
            initialState = ResultsState(
                useSimplePageView = deps.remoteConfig.useSimplePageView
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = { ExecutorImpl() },
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
        object UpdateFromDB : Action
        object UpdateCompleted : Action
        class InitialLoad(val queryParams: QueryParams) : Action
    }

    private sealed interface Msg {
        object StartLoading : Msg
        data class UpdateFromDB(val lectures: List<Lecture>) : Msg
        data class LoadingComplete(val state: ResultsState = ResultsState(isLoading = false)) : Msg
        data class FavoriteChanged(val id: Long, val isFavorite: Boolean) : Msg
        data class CurrentChanged(val id: Long, val isPlaying: Boolean) : Msg
    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
//            scope.launch {
//                dispatch(InitialLoad(settings.getFilters().addPage(deps.db)))
//            }

            deps.db.observeCompleted()
                .onEach { dispatch(UpdateCompleted) }
                .launchIn(scope)

            deps.db.observeAllFavorites()
                .onEach { dispatch(UpdateFromDB) }
                .launchIn(scope)

            deps.db.observeAllDownloads()
                .onEach { dispatch(UpdateFromDB) }
                .launchIn(scope)
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<ResultsIntent, Action, ResultsState, Msg, ResultsLabel>() {
        override fun executeAction(action: Action, getState: () -> ResultsState) {
            when (action) {
                is UpdateFromDB -> updateFromDB(getState())
                is UpdateCompleted -> updateCompleted(getState())
                else -> {
                    /** do nothing **/
                }
//                is InitialLoad -> {
//                    load(action.queryParams)
//                }
            }
        }

        override fun executeIntent(intent: ResultsIntent, getState: () -> ResultsState) {
            if (getState().isLoading) {
                Napier.d("executeIntent canceled, isLoading = true!", tag = "ResultsStoreExecutor")
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

                        Napier.d("LecturesLoaded -> ${newState.lectures.map { it.id }}", tag = "ResultsStore")
                        dispatch(Msg.LoadingComplete(newState))
                        publish(ResultsLabel.LecturesLoaded(newState.lectures))
                    }
                } else {
                    Napier.e(
                        message = "api.getResults isFailure ${result.exceptionOrNull()?.message}",
                        throwable = result.exceptionOrNull(),
                        tag = "ResultsStore"
                    )
                    dispatch(Msg.LoadingComplete())
                }
            }
        }

        private fun setFavorite(id: Long, isFavorite: Boolean, state: ResultsState) {
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
            ResultsState(
                isLoading = false,
                lectures = lectures(apiModel).updateFromDB(),
                pagination = pagination(apiModel),
            )

        private fun setCurrent(id: Long, isPlaying: Boolean, state: ResultsState) {
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

        private fun updateFromDB(state: ResultsState) =
            scope.launch {
                dispatch(Msg.UpdateFromDB(lectures = state.lectures.updateFromDB()))
            }

        private fun updateCompleted(state: ResultsState) =
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

    }

    private object ReducerImpl : Reducer<ResultsState, Msg> {
        override fun ResultsState.reduce(msg: Msg): ResultsState =
            when (msg) {
                Msg.StartLoading -> copy(isLoading = true)
                is Msg.FavoriteChanged -> update(id = msg.id) { copy(isFavorite = msg.isFavorite) }
                is Msg.UpdateFromDB -> copy(lectures = msg.lectures)
                is Msg.CurrentChanged -> copy(lectures = lectures.map { it.copy(isPlaying = it.id == msg.id && msg.isPlaying) })
                is Msg.LoadingComplete -> msg.state
            }

        private inline fun ResultsState.update(id: Long, func: Lecture.() -> Lecture): ResultsState {
            val lecture = lectures.find { it.id == id } ?: return this
            return put(lecture.func())
        }

        private fun ResultsState.put(lecture: Lecture): ResultsState {
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