package com.listentoprabhupada.common.lectures_api

import com.listentoprabhupada.common.data.Lecture

data class LecturesState(
    val isLoading: Boolean = false,
    val lectures: List<Lecture> = emptyList(),
    val pagination: Pagination = Pagination()
)
