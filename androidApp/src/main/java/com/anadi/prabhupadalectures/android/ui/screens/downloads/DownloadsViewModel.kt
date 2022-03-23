package com.anadi.prabhupadalectures.android.ui.screens.downloads

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.anadi.prabhupadalectures.android.coroutines.di.IODispatcher
import com.anadi.prabhupadalectures.android.coroutines.di.MainDispatcher
import com.anadi.prabhupadalectures.android.navigation.Router
import com.anadi.prabhupadalectures.android.ui.screens.CommonUiEvent
import com.anadi.prabhupadalectures.android.ui.screens.favorites.FavoritesEffect
import com.anadi.prabhupadalectures.android.ui.screens.favorites.FavoritesScreenState
import com.anadi.prabhupadalectures.android.viewmodel.BaseViewModel
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
class DownloadsViewModel @Inject constructor(
    context: Context,
    @MainDispatcher mainDispatcher: CoroutineDispatcher,
    @IODispatcher ioDispatcher: CoroutineDispatcher,
    private val resultsRepository: ResultsRepository,
    private val playbackRepository: PlaybackRepository,
    private val downloadsRepository: DownloadsRepository,
    private val toolsRepository: ToolsRepository,
    private val router: Router,
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
                CommonUiEvent.TappedBack -> { router.pop() }
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
