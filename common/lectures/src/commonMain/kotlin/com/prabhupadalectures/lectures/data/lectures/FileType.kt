package com.prabhupadalectures.lectures.data.lectures

data class FileType(
    val title: String = "", // Аудио
    val mimetypes: List<String> = emptyList() // audio/*
)
