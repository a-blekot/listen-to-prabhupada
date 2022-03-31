package com.anadi.prabhupadalectures.data.filters

private const val FILTERS_HEADER_NAME = ""
private const val FILTERS_HEADER_TITLE = "Filters"

val filtersHeader = Filter(
    name = FILTERS_HEADER_NAME,
    title = FILTERS_HEADER_TITLE,
)

data class Filter(
    val name: String = "",
    val title: String = "",
    val parent: String? = null,
    val options: List<Option> = emptyList(),
    val isExpanded: Boolean = true
)
