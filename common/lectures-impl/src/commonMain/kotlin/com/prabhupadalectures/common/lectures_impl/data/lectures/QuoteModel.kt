package com.prabhupadalectures.common.lectures_impl.data.lectures

data class QuoteModel(
    val scripture: String,
    val canto: String?,
    val chapter: Int,
    val verse: Int
)
