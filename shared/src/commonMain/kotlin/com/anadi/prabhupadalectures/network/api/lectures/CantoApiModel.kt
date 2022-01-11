package com.anadi.prabhupadalectures.network.api.lectures

import kotlinx.serialization.Serializable

@Serializable
data class CantoApiModel(
    val id: Int,
    val title: String,
    val scripture: Int,
    val aliases: List<String>
)