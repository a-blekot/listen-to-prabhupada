package com.anadi.prabhupadalectures.network.api.lectures

import kotlinx.serialization.Serializable

@Serializable
data class CategoryApiModel(
    val id: Int,
    val title: String,
    val aliases: List<String>
)