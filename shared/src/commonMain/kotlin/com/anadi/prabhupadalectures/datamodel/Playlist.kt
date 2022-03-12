package com.anadi.prabhupadalectures.datamodel

import com.anadi.prabhupadalectures.data.lectures.Lecture

data class Playlist(
    val lectures: List<Lecture> = emptyList()
): List<Lecture> by lectures