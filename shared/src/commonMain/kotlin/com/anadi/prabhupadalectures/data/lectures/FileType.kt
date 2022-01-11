package com.anadi.prabhupadalectures.data.lectures

data class FileType(
    val title: String = "", // Аудио
    val mimetypes: List<String> = emptyList() // audio/*
)
