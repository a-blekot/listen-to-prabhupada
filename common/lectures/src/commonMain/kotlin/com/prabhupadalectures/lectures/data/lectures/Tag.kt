package com.prabhupadalectures.lectures.data.lectures

data class Tag(
    val id: Int,
    val title: String,
    val aliases: List<String>
)