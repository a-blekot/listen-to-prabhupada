package com.listentoprabhupada.common.results_api

import com.listentoprabhupada.common.data.Lecture

data class ResultsState(
    val isLoading: Boolean = false,
    val lectures: List<Lecture> = emptyList(),
    val pagination: Pagination = Pagination(),
    val useSimplePageView: Boolean = true,
)
