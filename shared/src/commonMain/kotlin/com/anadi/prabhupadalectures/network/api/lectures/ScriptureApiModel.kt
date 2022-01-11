package com.anadi.prabhupadalectures.network.api.lectures

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ScriptureApiModel(
    val id: Int,
    val title: String,
    val aliases: List<String>, // "ШБ","Ш.Б.","Шримад Бхагаватам","ШБ"
    @SerialName("has_book")
    val hasCanto: Boolean
)