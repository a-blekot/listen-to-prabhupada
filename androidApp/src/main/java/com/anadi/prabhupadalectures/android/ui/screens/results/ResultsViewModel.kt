package com.anadi.prabhupadalectures.android.ui.screens.results

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.anadi.prabhupadalectures.android.coroutines.di.IODispatcher
import com.anadi.prabhupadalectures.android.coroutines.di.MainDispatcher
import com.anadi.prabhupadalectures.android.di.Route
import com.anadi.prabhupadalectures.android.navigation.Router
import com.anadi.prabhupadalectures.android.ui.screens.CommonUiEvent
import com.anadi.prabhupadalectures.android.viewmodel.BaseViewModel
import com.anadi.prabhupadalectures.repository.*
import com.anadi.prabhupadalectures.utils.ConnectionState
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResultsViewModel @Inject constructor(
    context: Context,
    @MainDispatcher mainDispatcher: CoroutineDispatcher,
    @IODispatcher ioDispatcher: CoroutineDispatcher,
    private val resultsRepository: ResultsRepository,
    private val playbackRepository: PlaybackRepository,
    private val downloadsRepository: DownloadsRepository,
    private val toolsRepository: ToolsRepository,
    private val router: Router,
) : BaseViewModel<CommonUiEvent, ResultsScreenState, ResultsEffect>(context, mainDispatcher, ioDispatcher) {

    private var p = 0

    init {
        observeState()
    }

    override fun setInitialState() = ResultsScreenState()

    override fun handleEvents(event: CommonUiEvent) {
        Napier.e("handleEvents")
        viewModelScope.launch {
            when (event) {
                is CommonUiEvent.ResultsEvent -> handleResultsEvent(event)

                CommonUiEvent.TappedBack -> router.pop()
                is CommonUiEvent.Favorite -> toolsRepository.setFavorite(event.lecture, event.isFavorite)
                is CommonUiEvent.Player -> {
                    when (event.action) {
                        is Download -> downloadsRepository.download(event.action.lecture)
                        else -> playbackRepository.handleAction(event.action)
                    }
                }
            }
        }
    }

    override fun onConnectivityStateChanged(connectionState: ConnectionState) {
        when (connectionState) {
            ConnectionState.Online -> {
                setState { copy(isOnline = true) }
                viewModelScope.launch {
                    resultsRepository.init()
//                    setEffect { ResultsEffect.Toast("Results are loaded.") }
                }
            }
            ConnectionState.Offline -> {
                setState { copy(isOnline = false) }
            }
        }
    }

    private suspend fun handleResultsEvent(event: CommonUiEvent.ResultsEvent) {
        when (event) {
            CommonUiEvent.ResultsEvent.OpenDownloads -> router.push(Route.Downloads)
            CommonUiEvent.ResultsEvent.OpenFavorites -> router.push(Route.Favorites)
            is CommonUiEvent.ResultsEvent.Expand -> toolsRepository.saveExpanded(event.filterName, event.isExpanded)
            is CommonUiEvent.ResultsEvent.Option -> resultsRepository.updateQuery(event.queryParam)
            is CommonUiEvent.ResultsEvent.Page -> resultsRepository.updatePage(event.page)
        }
    }

    private fun observeState() =
        viewModelScope.launch {
            resultsRepository.observeState().combine(
                playbackRepository.observeState()
            ) { results, playback ->
                setState {
                    copy(results = results, playback = playback)
                }
                if (results.lectures.isEmpty() && !results.isLoading) {
                    setEffect {
                        ResultsEffect.Toast("There is no lectures for selected filters")
                    }
                }
            }
                .collect()
        }
}