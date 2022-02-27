package com.anadi.prabhupadalectures.datamodel

import com.anadi.prabhupadalectures.data.lectures.Lecture

data class Playlist(
    val lectures: List<Lecture> = emptyList(),
    val currentId: Long = lectures.firstOrNull()?.id ?: 0L,
    val currentIndex: Int = lectures.indexOfFirst { it.id == currentId },
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
        return copy(currentId = lectures.getOrNull(currentIndex - 1)?.id ?: 0L)
    }

    fun next(): Playlist {
        if (!hasNext) return this
        return copy(currentId = lectures.getOrNull(currentIndex + 1)?.id ?: 0L)
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