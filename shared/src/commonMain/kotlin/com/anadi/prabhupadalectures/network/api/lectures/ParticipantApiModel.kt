package com.anadi.prabhupadalectures.network.api.lectures

import kotlinx.serialization.Serializable

@Serializable
data class ParticipantApiModel(
    val id: Int,
    val name: String,
    val photo: String?,
    val aliases: List<String>,
    val description: String
)