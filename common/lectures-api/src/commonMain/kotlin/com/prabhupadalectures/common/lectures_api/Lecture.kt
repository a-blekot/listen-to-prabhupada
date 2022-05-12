package com.prabhupadalectures.common.lectures_api

data class Lecture(
    val id: Long = 0L,
    val title: String = "",
    val description: String? = null,
    val date: String = "",
    val place: String = "",
    val durationMillis: Long = 0L,
    val fileUrl: String? = null,
    val remoteUrl: String = "",
    val isFavorite: Boolean = false,
    val isCompleted: Boolean = false,
    val isPlaying: Boolean = false,
    val downloadProgress: Int? = null
) {
    val subTitle
        get() = "$date, $place"

    val displayedDescription
        get() = description ?: subTitle
}
