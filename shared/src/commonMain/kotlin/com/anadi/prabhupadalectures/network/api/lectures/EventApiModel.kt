package com.anadi.prabhupadalectures.network.api.lectures

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EventApiModel(
    val id: Int,
    @SerialName("combine_name")
    val name: String
)