package com.prabhupadalectures.common.lectures_api

import com.prabhupadalectures.common.utils.Lecture

data class LecturesState(
    val isLoading: Boolean = false,
    val lectures: List<Lecture> = emptyList(),
    val pagination: Pagination = Pagination()
)
