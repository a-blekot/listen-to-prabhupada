package com.listentoprabhupada.common.filters.data

import kotlinx.serialization.Serializable

@Serializable
data class Option(
    val value: String = "",
    val text: String = "",
    val isSelected: Boolean = false
)
