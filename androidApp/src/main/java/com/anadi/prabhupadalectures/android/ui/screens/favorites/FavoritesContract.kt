package com.anadi.prabhupadalectures.android.ui.screens.favorites

import com.anadi.prabhupadalectures.android.viewmodel.UiEffect
import com.anadi.prabhupadalectures.android.viewmodel.UiState
import com.anadi.prabhupadalectures.data.lectures.Lecture
import com.anadi.prabhupadalectures.repository.PlaybackState

data class FavoritesScreenState(
    val favorites: List<Lecture> = emptyList(),
    val playback: PlaybackState = PlaybackState()
) : UiState

sealed class FavoritesEffect : UiEffect {
    data class Toast(val message: String) : FavoritesEffect()
}