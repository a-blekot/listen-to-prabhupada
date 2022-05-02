package com.prabhupadalectures.android.ui.screens.downloads

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.prabhupadalectures.android.base.viewmodel.BaseViewModel
import com.prabhupadalectures.lectures.events.CommonUiEvent
import com.prabhupadalectures.lectures.events.Download
import com.prabhupadalectures.lectures.repository.DownloadsRepository
import com.prabhupadalectures.lectures.repository.PlaybackRepository
import com.prabhupadalectures.lectures.repository.ResultsRepository
import com.prabhupadalectures.lectures.repository.ToolsRepository
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class DownloadsViewModel(
    context: Context,
    mainDispatcher: CoroutineDispatcher,
    ioDispatcher: CoroutineDispatcher,
    private val resultsRepository: ResultsRepository,
    private val playbackRepository: PlaybackRepository,
    private val downloadsRepository: DownloadsRepository,
    private val toolsRepository: ToolsRepository,
) : BaseViewModel<CommonUiEvent, DownloadsScreenState, DownloadsEffect>(context, mainDispatcher, ioDispatcher) {

    init {
        observeState()
    }

    override fun onCleared() {
        super.onCleared()
        resultsRepository.capturePlayback()
    }

    override fun setInitialState() = DownloadsScreenState()

    override fun handleEvents(event: CommonUiEvent) {
        Napier.e("DownloadsViewModel handleEvents")
        viewModelScope.launch {
            when (event) {
                CommonUiEvent.TappedBack -> {
//                    router.pop()
                }
                is CommonUiEvent.Favorite -> toolsRepository.setFavorite(event.lecture, event.isFavorite)
                is CommonUiEvent.Player -> {
                    when (event.action) {
                        is Download -> downloadsRepository.download((event.action as Download).lecture)
                        else -> playbackRepository.handleAction(event.action)
                    }
                }
                else -> {
                    /** do nothing**/
                }
            }
        }
    }

    private fun observeState() =
        viewModelScope.launch {

            toolsRepository.observeDownloads().combine(
                playbackRepository.observeState()
            ) { downloads, playback ->
                setState {
                    copy(downloads = downloads, playback = playback)
                }

                if (state.value.downloads != downloads) {
                    playbackRepository.updatePlaylist(downloads)
                }

                if (downloads.isEmpty()) {
                    setEffect {
                        DownloadsEffect.Toast("There is no downloads")
                    }
                }
            }
                .collect()
        }
}
