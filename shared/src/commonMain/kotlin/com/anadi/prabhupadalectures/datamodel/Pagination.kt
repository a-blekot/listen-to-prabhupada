package com.anadi.prabhupadalectures.datamodel

import com.anadi.prabhupadalectures.network.api.ApiModel

data class Pagination(
    val prev: Int? = null,
    val curr: Int = 1,
    val next: Int? = null
) {
    constructor(apiModel: ApiModel) : this(
        prev = apiModel.prevPage,
        curr = 1,
        next = apiModel.nextPage
    )
}