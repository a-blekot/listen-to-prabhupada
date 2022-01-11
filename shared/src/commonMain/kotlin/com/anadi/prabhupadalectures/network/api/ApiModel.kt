package com.anadi.prabhupadalectures.network.api

import com.anadi.prabhupadalectures.network.api.lectures.ResultsApiModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiModel(
    val count: Int,
    val next: String?,
    @SerialName("previous")
    val prev: String?,
    val results: ResultsApiModel
) {
    val nextPage
        get() = next?.pageNumber

    val prevPage
        get() = prev?.pageNumber
}

private val String.pageNumber
    get() = split("?page=").getOrNull(1)?.toIntOrNull()