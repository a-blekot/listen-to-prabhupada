package com.prabhupadalectures.common.player_api

import com.arkivanov.decompose.value.Value
import com.prabhupadalectures.common.utils.Lecture

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
    fun onDownload(lecture: Lecture) {}

    sealed class Output {
        data class Message(val text: String) : Output()
    }
}