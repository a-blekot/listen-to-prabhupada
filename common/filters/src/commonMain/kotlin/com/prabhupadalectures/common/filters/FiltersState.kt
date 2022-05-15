package com.prabhupadalectures.common.filters

import com.prabhupadalectures.common.filters.data.Filter

data class FiltersState(
    val isLoading: Boolean = false,
    val filters: List<Filter> = emptyList(),
    val totalLecturesCount: Int = 0,
    val pagesCount: Int = 0,
)