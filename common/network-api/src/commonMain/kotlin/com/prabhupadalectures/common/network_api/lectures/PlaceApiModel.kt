package com.prabhupadalectures.common.network_api.lectures

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlaceApiModel(
    @SerialName("combine_name")
    val name: String,
    val id: Int
)