package com.listentoprabhupada.common.filters_api

data class QueryParam(
    val filterName: String = "",
    val selectedOption: String = "",
    val isSelected: Boolean = false
)