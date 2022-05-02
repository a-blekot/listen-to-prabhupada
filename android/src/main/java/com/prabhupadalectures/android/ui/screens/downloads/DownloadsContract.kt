package com.prabhupadalectures.android.ui.screens.downloads

import com.prabhupadalectures.lectures.data.lectures.Lecture
import com.prabhupadalectures.lectures.events.UiEffect
import com.prabhupadalectures.lectures.events.UiState
import com.prabhupadalectures.lectures.repository.PlaybackState

data class DownloadsScreenState(
    val downloads: List<Lecture> = emptyList(),
    val playback: PlaybackState = PlaybackState()
) : UiState

sealed class DownloadsEffect : UiEffect {
    data class Toast(val message: String) : DownloadsEffect()
}