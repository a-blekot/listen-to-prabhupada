package com.prabhupadalectures.android.ui.screens.favorites

import com.prabhupadalectures.lectures.data.lectures.Lecture
import com.prabhupadalectures.lectures.events.UiEffect
import com.prabhupadalectures.lectures.events.UiState
import com.prabhupadalectures.lectures.repository.PlaybackState

data class FavoritesScreenState(
    val favorites: List<Lecture> = emptyList(),
    val playback: PlaybackState = PlaybackState()
) : UiState

sealed class FavoritesEffect : UiEffect {
    data class Toast(val message: String) : FavoritesEffect()
}