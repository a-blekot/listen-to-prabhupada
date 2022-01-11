package com.anadi.prabhupadalectures.network.api.lectures

import kotlinx.serialization.Serializable

@Serializable
data class PeriodApiModel(
    val id: Int,
    val title: String
)