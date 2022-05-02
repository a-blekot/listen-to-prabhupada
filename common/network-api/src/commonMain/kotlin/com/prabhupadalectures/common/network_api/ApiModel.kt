package com.prabhupadalectures.common.network_api

import com.prabhupadalectures.common.network_api.lectures.ResultsApiModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

const val FIRST_PAGE = 1

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
        get() = prev?.let { it.pageNumber ?: FIRST_PAGE }
}

val String.pageNumber
    get() =
        split("page=")
            .getOrNull(1)?.let {
                (it.split("&").getOrNull(0) ?: it).toIntOrNull()
            }
