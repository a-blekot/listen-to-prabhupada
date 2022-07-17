package com.listentoprabhupada.common.network_api.lectures

import kotlinx.serialization.Serializable

@Serializable
data class CategoryApiModel(
    val id: Int,
    val title: String,
    val aliases: List<String>
)