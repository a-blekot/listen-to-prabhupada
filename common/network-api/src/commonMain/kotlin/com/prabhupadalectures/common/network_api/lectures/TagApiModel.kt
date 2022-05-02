package com.prabhupadalectures.common.network_api.lectures

import kotlinx.serialization.Serializable

@Serializable
data class TagApiModel(
    val id: Int,
    val title: String,
    val aliases: List<String>
)