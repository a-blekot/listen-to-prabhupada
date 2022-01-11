package com.anadi.prabhupadalectures.network.api.filters

import kotlinx.serialization.Serializable

@Serializable
data class OptionApiModel(
    val value: String,// "1",             "5"
    val text: String, // "Бхагавад-гита", "Нектар преданности"
    val selected: Boolean
)