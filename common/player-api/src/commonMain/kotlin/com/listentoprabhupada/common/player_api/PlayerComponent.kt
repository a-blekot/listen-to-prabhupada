package com.listentoprabhupada.common.player_api

import com.arkivanov.decompose.value.Value

interface PlayerComponent {

    val flow: Value<PlayerState>

    fun onPause() {}
    fun onNext() {}
    fun onPrev() {}
    fun onSeekForward() {}
    fun onSeekBack() {}
    fun onSliderReleased() {}

    fun onPlay(id: Long) {}
    fun onSeekTo(timeMs: Long) {}
    fun onSpeed(speed: Float) {}

}