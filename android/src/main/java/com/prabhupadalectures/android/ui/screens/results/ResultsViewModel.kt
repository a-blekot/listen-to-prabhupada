//package com.prabhupadalectures.android.ui.screens.results
//
//import android.content.Context
//import androidx.lifecycle.viewModelScope
//import com.prabhupadalectures.android.PrabhupadaApp.Companion.app
//import com.prabhupadalectures.android.base.viewmodel.BaseViewModel
//import com.prabhupadalectures.common.database.Database
//import com.prabhupadalectures.lectures.data.filters.filtersHeader
//import com.prabhupadalectures.lectures.events.CommonUiEvent
//import com.prabhupadalectures.lectures.events.Download
//import com.prabhupadalectures.lectures.repository.DownloadsRepository
//import com.prabhupadalectures.lectures.repository.PlaybackRepository
//import com.prabhupadalectures.lectures.repository.ResultsRepository
//import com.prabhupadalectures.lectures.repository.ToolsRepository
//import com.prabhupadalectures.lectures.utils.ConnectionState
//import com.prabhupadalectures.lectures.utils.ShareAction
//import io.github.aakira.napier.Napier
//import kotlinx.coroutines.CoroutineDispatcher
//import kotlinx.coroutines.flow.combine
//import kotlinx.coroutines.flow.collect
//import kotlinx.coroutines.launch
//
//class ResultsViewModel(
//    context: Context,
//    mainDispatcher: CoroutineDispatcher,
//    ioDispatcher: CoroutineDispatcher,
//    private val resultsRepository: ResultsRepository,
//    private val playbackRepository: PlaybackRepository,
//    private val downloadsRepository: DownloadsRepository,
//    private val toolsRepository: ToolsRepository,
//    private val db: Database,
//) : BaseViewModel<CommonUiEvent, ResultsScreenState, ResultsEffect>(context, mainDispatcher, ioDispatcher) {
//
//    init {
//        observeState()
//    }
//
//    override fun setInitialState() = ResultsScreenState()
//
//    override fun handleEvents(event: CommonUiEvent) {
//        Napier.e("handleEvents")
//        viewModelScope.launch {
//            when (event) {
//                is CommonUiEvent.ResultsEvent -> handleResultsEvent(event)
//
//                CommonUiEvent.TappedBack -> {
////                    router.pop()
//                }
//                is CommonUiEvent.Share -> share(event.lectureId)
//                is CommonUiEvent.Favorite -> toolsRepository.setFavorite(event.lecture, event.isFavorite)
//                is CommonUiEvent.Player -> {
//                    when (event.action) {
//                        is Download -> downloadsRepository.download((event.action as Download).lecture)
//                        else -> playbackRepository.handleAction(event.action)
//                    }
//                }
//            }
//        }
//    }
//
//    override fun onConnectivityStateChanged(connectionState: ConnectionState) {
//        when (connectionState) {
//            ConnectionState.Online -> {
//                setState { copy(isOnline = true) }
//                viewModelScope.launch {
//                    resultsRepository.init(app.shareAction)
////                    setEffect { ResultsEffect.Toast("Results are loaded.") }
//                }
//            }
//            ConnectionState.Offline -> {
//                setState { copy(isOnline = false) }
//            }
//        }
//    }
//
//    fun isFiltersHeaderExpanded() =
//        db.selectExpandedFilter(filtersHeader.name)
//
//    private suspend fun handleResultsEvent(event: CommonUiEvent.ResultsEvent) {
//        when (event) {
//            CommonUiEvent.ResultsEvent.OpenDownloads -> {
////                router.push(Route.Downloads)
//            }
//            CommonUiEvent.ResultsEvent.OpenFavorites -> {
////                router.push(Route.Favorites)
//            }
//            is CommonUiEvent.ResultsEvent.Expand -> toolsRepository.saveExpanded(event.filterName, event.isExpanded)
//            is CommonUiEvent.ResultsEvent.Option -> resultsRepository.updateQuery(event.queryParam)
//            is CommonUiEvent.ResultsEvent.ClearAllFilters -> resultsRepository.clearAllFilters()
//            is CommonUiEvent.ResultsEvent.Page -> resultsRepository.updatePage(event.page)
//        }
//    }
//
//    private fun observeState() =
//        viewModelScope.launch {
//            resultsRepository.observeState().combine(
//                playbackRepository.observeState()
//            ) { results, playback ->
//                setState {
//                    copy(results = results, playback = playback)
//                }
////                if (results != state.value.results && results.lectures.isEmpty() && !results.isLoading) {
////                    setEffect {
////                        ResultsEffect.Toast("There is no lectures for selected filters")
////                    }
////                }
//            }
//                .collect()
//        }
//
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