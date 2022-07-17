package com.listentoprabhupada.common.lectures_impl.data.lectures

data class FileType(
    val title: String = "", // Аудио
    val mimetypes: List<String> = emptyList() // audio/*
)
