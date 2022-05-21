package com.prabhupadalectures.common.lectures_api

import kotlin.native.concurrent.SharedImmutable

@SharedImmutable
const val LECTURES_PER_PAGE = 6

data class Pagination(
    val prev: Int? = null,
    val curr: Int = 1,
    val next: Int? = null,
    val total: Int = 1
) {
    private val range
        get() = 1..total

    fun add(pages: Int) = curr + pages

    fun canAdd(pages: Int) =
        add(pages) in range
}
