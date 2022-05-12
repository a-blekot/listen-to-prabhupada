package com.prabhupadalectures.common.lectures_api

data class LecturesState(
    val isLoading: Boolean = false,
    val lectures: List<Lecture> = emptyList(),
    val pagination: Pagination = Pagination()
)
