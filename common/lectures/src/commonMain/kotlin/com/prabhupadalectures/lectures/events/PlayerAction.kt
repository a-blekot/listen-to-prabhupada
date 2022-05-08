package com.prabhupadalectures.lectures.events

import com.prabhupadalectures.lectures.data.lectures.Lecture

sealed class PlayerAction
data class Download(val lecture: Lecture): PlayerAction()
data class Play(val lectureId: Long): PlayerAction()
object Pause: PlayerAction()
object Next: PlayerAction()
object Prev: PlayerAction()
object SeekForward: PlayerAction()
object SeekBack: PlayerAction()
data class SeekTo(val timeMs: Long): PlayerAction()
object SliderReleased: PlayerAction()