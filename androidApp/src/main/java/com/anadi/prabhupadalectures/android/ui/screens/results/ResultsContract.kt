package com.anadi.prabhupadalectures.android.ui.screens.results

import com.anadi.prabhupadalectures.android.base.viewmodel.UiEffect
import com.anadi.prabhupadalectures.android.base.viewmodel.UiState
import com.anadi.prabhupadalectures.repository.PlaybackState
import com.anadi.prabhupadalectures.repository.ResultsState
import com.anadi.prabhupadalectures.utils.ShareAction

data class ResultsScreenState(
    val results: ResultsState = ResultsState(),
    val playback: PlaybackState = PlaybackState(),
    val isOnline: Boolean = true
) : UiState

sealed class ResultsEffect : UiEffect {
    data class Toast(val message: String) : ResultsEffect()
    data class Share(val shareAction: ShareAction) : ResultsEffect()
}