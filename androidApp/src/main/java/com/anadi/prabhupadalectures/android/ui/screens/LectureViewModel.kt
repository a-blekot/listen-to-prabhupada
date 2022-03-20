package com.anadi.prabhupadalectures.android.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anadi.prabhupadalectures.data.lectures.Lecture
import com.anadi.prabhupadalectures.repository.PlaybackRepository
import com.anadi.prabhupadalectures.repository.PlayerAction
import com.anadi.prabhupadalectures.repository.ToolsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LectureViewModel @Inject constructor(
    private val playbackRepository: PlaybackRepository,
    private val toolsRepository: ToolsRepository
) : ViewModel() {

    fun handleAction(playerAction: PlayerAction) =
        viewModelScope.launch {
            playbackRepository.handleAction(playerAction)
        }

    fun setFavorite(lecture: Lecture, isFavorite: Boolean) =
        toolsRepository.setFavorite(lecture, isFavorite)
}