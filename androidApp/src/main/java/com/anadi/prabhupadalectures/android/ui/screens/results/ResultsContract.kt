package com.anadi.prabhupadalectures.android.ui.screens.results

import com.anadi.prabhupadalectures.android.viewmodel.UiEffect
import com.anadi.prabhupadalectures.android.viewmodel.UiState
import com.anadi.prabhupadalectures.repository.PlaybackState
import com.anadi.prabhupadalectures.repository.ResultsState

data class ResultsScreenState(
    val results: ResultsState = ResultsState(),
    val playback: PlaybackState = PlaybackState(),
    val isOnline: Boolean = true
) : UiState

sealed class ResultsEffect : UiEffect {
    data class Toast(val message: String) : ResultsEffect()
}