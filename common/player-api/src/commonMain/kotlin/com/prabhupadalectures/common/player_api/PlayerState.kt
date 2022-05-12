package com.prabhupadalectures.common.player_api

import com.prabhupadalectures.common.lectures_api.Lecture

data class PlayerState(
    val lecture: Lecture = Lecture(),
    val isPlaying: Boolean = false,
    val isBuffering: Boolean = false,
    val hasNext: Boolean = false,
    val hasPrevious: Boolean = false,
    val timeMs: Long = 0L,
    val durationMs: Long = 1L
)