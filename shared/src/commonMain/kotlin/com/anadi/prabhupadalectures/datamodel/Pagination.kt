package com.anadi.prabhupadalectures.datamodel

import com.anadi.prabhupadalectures.network.api.ApiModel
import com.anadi.prabhupadalectures.repository.FIRST_PAGE

data class Pagination(
    val prev: Int? = null,
    val curr: Int = 1,
    val next: Int? = null
) {
    constructor(apiModel: ApiModel) : this(
        prev = apiModel.prevPage,
        curr = currentPage(apiModel),
        next = apiModel.nextPage
    )
}

private fun currentPage(apiModel: ApiModel) =
    apiModel.prevPage?.let { it + 1 }
        ?: apiModel.nextPage?.let { it - 1 }
        ?: FIRST_PAGE