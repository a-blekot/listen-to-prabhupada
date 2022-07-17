package com.listentoprabhupada.common.filters.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

@Serializable
data class Filter(
    val name: String = "",
    val title: String = "",
    val parent: String? = null,
    val options: List<Option> = emptyList(),
    val isExpanded: Boolean = true
)

fun List<Filter>.encodeToString(): String =
    Json.encodeToString(serializer(), this)

fun String.decodeFiltersList(): List<Filter> =
    if (isBlank()) {
        emptyList()
    } else {
        Json.decodeFromString(serializer(), this)
    }
