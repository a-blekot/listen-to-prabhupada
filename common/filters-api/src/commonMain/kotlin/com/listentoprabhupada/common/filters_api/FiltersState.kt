package com.listentoprabhupada.common.filters_api

data class FiltersState(
    val isLoading: Boolean = false,
    val filters: List<Filter> = emptyList(),
    val totalLecturesCount: Int = 0,
    val pagesCount: Int = 0,
)