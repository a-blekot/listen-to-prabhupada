package com.prabhupadalectures.common.network_api.lectures

import kotlinx.serialization.Serializable

@Serializable
data class PeriodApiModel(
    val id: Int,
    val title: String
)