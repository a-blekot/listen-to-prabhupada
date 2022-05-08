package com.prabhupadalectures.android.ui.screens.favorites

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.prabhupadalectures.android.base.viewmodel.BaseViewModel
import com.prabhupadalectures.lectures.events.CommonUiEvent
import com.prabhupadalectures.lectures.events.Download
import com.prabhupadalectures.lectures.repository.DownloadsRepository
import com.prabhupadalectures.lectures.repository.PlaybackRepository
import com.prabhupadalectures.lectures.repository.ToolsRepository
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class FavoritesViewModel(
    context: Context,
    mainDispatcher: CoroutineDispatcher,
    ioDispatcher: CoroutineDispatcher,
//    private val resultsRepository: ResultsRepository,
    private val playbackRepository: PlaybackRepository,
    private val downloadsRepository: DownloadsRepository,
    private val toolsRepository: ToolsRepository,
) : BaseViewModel<CommonUiEvent, FavoritesScreenState, FavoritesEffect>(context, mainDispatcher, ioDispatcher) {

    init {
        observeState()
        observePlaylist()
    }

    override fun onCleared() {
        super.onCleared()
//        resultsRepository.capturePlayback()
    }

    override fun setInitialState() = FavoritesScreenState()

    override fun handleEvents(event: CommonUiEvent) {
        Napier.e("handleEvents")
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

            toolsRepository.observeFavorites().combine(
                playbackRepository.observeState()
            ) { favorites, playback ->
                setState {
                    copy(favorites = favorites, playback = playback)
                }
                if (favorites.isEmpty()) {
                    setEffect {
                        FavoritesEffect.Toast("There is no favorites")
                    }
                }
            }
                .collect()
        }

    private fun observePlaylist() =
        viewModelScope.launch {
            toolsRepository.observeFavorites()
                .onEach {
                    playbackRepository.updatePlaylist(it)
                }
                .collect()
        }
}
