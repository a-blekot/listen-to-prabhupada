package com.anadi.prabhupadalectures.data.filters

data class Filter(
    val name: String = "",
    val title: String = "",
    val parent: String? = null,
    val options: List<Option> = emptyList()
)
