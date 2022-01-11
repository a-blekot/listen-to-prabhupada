package com.anadi.prabhupadalectures.data.lectures

data class QuoteModel(
    val scripture: String,
    val canto: String?,
    val chapter: Int,
    val verse: Int
)
