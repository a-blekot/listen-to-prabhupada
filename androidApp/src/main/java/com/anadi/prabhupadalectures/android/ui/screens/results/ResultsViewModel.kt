package com.anadi.prabhupadalectures.android.ui.screens.results

import androidx.lifecycle.viewModelScope
import com.anadi.prabhupadalectures.android.coroutines.di.IODispatcher
import com.anadi.prabhupadalectures.android.coroutines.di.MainDispatcher
import com.anadi.prabhupadalectures.android.navigation.Router
import com.anadi.prabhupadalectures.android.viewmodel.BaseViewModel
import com.anadi.prabhupadalectures.network.api.Error
import com.anadi.prabhupadalectures.network.api.Progress
import com.anadi.prabhupadalectures.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResultsViewModel @Inject constructor(
    @MainDispatcher mainDispatcher: CoroutineDispatcher,
    @IODispatcher ioDispatcher: CoroutineDispatcher,
    private val resultsRepository: ResultsRepository,
    private val playbackRepository: PlaybackRepository,
    private val downloadsRepository: DownloadsRepository,
    private val toolsRepository: ToolsRepository,
    private val router: Router,
) : BaseViewModel<ResultsEvent, ResultsScreenState, ResultsEffect>(mainDispatcher, ioDispatcher, 500L) {

    private var p = 0

    init {
        viewModelScope.launch {
            resultsRepository.init()
            setEffect { ResultsEffect.Toast("Results are loaded.") }
        }

        observeState()
        observeDownloads()
    }

    override fun setInitialState() = ResultsScreenState()

    override fun handleEvents(event: ResultsEvent) {
        Napier.e("handleEvents")
        viewModelScope.launch {
            when (event) {
                ResultsEvent.TappedBack -> router.pop()
                is ResultsEvent.Favorite -> toolsRepository.setFavorite(event.lecture, event.isFavorite)
                is ResultsEvent.Expand -> toolsRepository.saveExpanded(event.filterName, event.isExpanded)
                is ResultsEvent.Option -> resultsRepository.updateQuery(event.queryParam)
                is ResultsEvent.Page -> resultsRepository.updatePage(event.page)
                is ResultsEvent.Player -> {
                    when (event.action) {
                        is Download -> downloadsRepository.download(event.action.lecture)
                        else -> playbackRepository.handleAction(event.action)
                    }
                }
            }
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
            }
                .collect()
        }

    private fun observeDownloads() =
        viewModelScope.launch {
            downloadsRepository.observeState()
                .onEach {
                    when (it) {
                        is Progress -> {
                            if (it.progress - p > 10) {
                                setEffect {
                                    ResultsEffect.Toast("Progress = ${it.progress}%")
                                }
                            }
                            p = it.progress
                        }
                        is Error -> {
                            setEffect {
                                ResultsEffect.Toast("Error: ${it.t?.message}")
                            }
                        }
                        else -> {
                            /** do nothing **/
                        }
                    }
                }
                .collect()
        }

}