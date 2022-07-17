package com.listentoprabhupada.common.network_api.lectures

import kotlinx.serialization.Serializable

@Serializable
data class PeriodApiModel(
    val id: Int,
    val title: String
)