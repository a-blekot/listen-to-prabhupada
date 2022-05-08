package com.prabhupadalectures.lectures.data

import com.prabhupadalectures.common.network_api.ApiModel
import com.prabhupadalectures.common.settings.FIRST_PAGE

const val LECTURES_PER_PAGE = 6

data class Pagination(
    val prev: Int? = null,
    val curr: Int = 1,
    val next: Int? = null,
    val total: Int = 1
) {
    constructor(apiModel: ApiModel) : this(
        prev = apiModel.prevPage,
        curr = currentPage(apiModel),
        next = apiModel.nextPage,
        total = totalPages(apiModel.count)
    )

    private val range
        get() = 1..total

    fun add(pages: Int) = curr + pages

    fun canAdd(pages: Int) =
        add(pages) in range
}

private fun currentPage(apiModel: ApiModel) =
    apiModel.prevPage?.let { it + 1 }
        ?: apiModel.nextPage?.let { it - 1 }
        ?: FIRST_PAGE

private fun totalPages(totalLectures: Int) =
    totalLectures / LECTURES_PER_PAGE + if (totalLectures % LECTURES_PER_PAGE == 0) 0 else 1