package com.anadi.prabhupadalectures.android.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anadi.prabhupadalectures.repository.Download
import com.anadi.prabhupadalectures.repository.DownloadsRepository
import com.anadi.prabhupadalectures.repository.PlaybackRepository
import com.anadi.prabhupadalectures.repository.PlayerAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val playbackRepository: PlaybackRepository,
    private val downloadsRepository: DownloadsRepository
) : ViewModel() {

    fun observePlayback() =
        playbackRepository.observeState()

    fun handle(playerAction: PlayerAction) =
        viewModelScope.launch {
            when (playerAction) {
                is Download -> downloadsRepository.download(playerAction.lecture)
                else -> playbackRepository.handleAction(playerAction)
            }
        }
}