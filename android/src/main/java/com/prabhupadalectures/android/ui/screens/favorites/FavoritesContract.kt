//package com.prabhupadalectures.android.ui.screens.favorites
//
//import com.prabhupadalectures.common.lectures_api.Lecture
//import com.prabhupadalectures.common.lectures_impl.events.UiEffect
//import com.prabhupadalectures.common.lectures_impl.events.UiState
//import com.prabhupadalectures.common.player_api.PlayerState
//
//data class FavoritesScreenState(
//    val favorites: List<Lecture> = emptyList(),
//    val playback: PlayerState = PlayerState()
//) : UiState
//
//sealed class FavoritesEffect : UiEffect {
//    data class Toast(val message: String) : FavoritesEffect()
//}