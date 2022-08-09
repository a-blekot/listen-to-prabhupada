package com.listentoprabhupada.common.player_api

sealed interface PlayerAction {
    object Pause: PlayerAction
    object Next: PlayerAction
    object Prev: PlayerAction
    object SeekForward: PlayerAction
    object SeekBack: PlayerAction
    object SliderReleased: PlayerAction

    data class Play(val lectureId: Long): PlayerAction
    data class SeekTo(val timeMs: Long): PlayerAction
    data class Speed(val speed: Float): PlayerAction
}
