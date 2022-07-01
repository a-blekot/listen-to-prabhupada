//package com.prabhupadalectures.android.ui.screens.downloads
//
//import com.prabhupadalectures.common.utils.Lecture
//import com.prabhupadalectures.common.lectures_impl.events.UiEffect
//import com.prabhupadalectures.common.lectures_impl.events.UiState
//import com.prabhupadalectures.common.player_api.PlayerState
//
//data class DownloadsScreenState(
//    val downloads: List<Lecture> = emptyList(),
//    val playback: PlayerState = PlayerState()
//) : UiState
//
//sealed class DownloadsEffect : UiEffect {
//    data class Toast(val message: String) : DownloadsEffect()
//}