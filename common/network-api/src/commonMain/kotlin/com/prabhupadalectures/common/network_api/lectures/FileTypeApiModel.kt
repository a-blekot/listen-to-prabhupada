package com.prabhupadalectures.common.network_api.lectures

import kotlinx.serialization.Serializable

@Serializable
data class FileTypeApiModel(
    val id: Int,
    val title: String, // Аудио
    val mimetypes: List<String> // audio/*
)