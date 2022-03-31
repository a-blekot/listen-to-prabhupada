package com.anadi.prabhupadalectures.android.ui.screens.downloads

import com.anadi.prabhupadalectures.android.base.viewmodel.UiEffect
import com.anadi.prabhupadalectures.android.base.viewmodel.UiState
import com.anadi.prabhupadalectures.data.lectures.Lecture
import com.anadi.prabhupadalectures.repository.PlaybackState

data class DownloadsScreenState(
    val downloads: List<Lecture> = emptyList(),
    val playback: PlaybackState = PlaybackState()
) : UiState

sealed class DownloadsEffect : UiEffect {
    data class Toast(val message: String) : DownloadsEffect()
}