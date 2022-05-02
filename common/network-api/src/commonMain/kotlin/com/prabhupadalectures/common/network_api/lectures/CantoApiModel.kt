package com.prabhupadalectures.common.network_api.lectures

import kotlinx.serialization.Serializable

@Serializable
data class CantoApiModel(
    val id: Int,
    val title: String,
    val scripture: Int,
    val aliases: List<String>
)