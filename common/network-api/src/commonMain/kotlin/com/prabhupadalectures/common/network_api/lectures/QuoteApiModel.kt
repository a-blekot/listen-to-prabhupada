package com.listentoprabhupada.common.network_api.lectures

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuoteApiModel(
    val id: Int,
    val scripture: ScriptureApiModel,
    @SerialName("book")
    val canto: CantoApiModel?,
    val chapter: Int?,
    val verse: Int?
)
