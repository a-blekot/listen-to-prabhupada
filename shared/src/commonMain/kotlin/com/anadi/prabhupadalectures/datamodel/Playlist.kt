package com.anadi.prabhupadalectures.datamodel

import com.anadi.prabhupadalectures.data.lectures.Lecture

enum class PlaybackState {
    PLAYING, PAUSED
}

private const val NO_INDEX = -1

data class Playlist(
    val lectures: List<Lecture> = emptyList(),
    val currentIndex: Int = 0,
    val isPlaying: Boolean = false
) {
    val isEmpty
        get() = lectures.isEmpty()

    val hasPrev
        get() = !isEmpty && currentIndex > 0

    val hasNext
        get() = !isEmpty && currentIndex < lectures.lastIndex

    val currentLecture
        get() = lectures.getOrNull(currentIndex)

    fun prev(): Playlist {
        if (!hasPrev) return this
        return copy(currentIndex = currentIndex - 1)
    }

    fun next(): Playlist {
        if (!hasNext) return this
        return copy(currentIndex = currentIndex + 1)
    }

    fun play() =
        when {
            !isPlaying -> copy(isPlaying = true)
            else -> this
        }

    fun pause() =
        when {
            isPlaying -> copy(isPlaying = false)
            else -> this
        }
}