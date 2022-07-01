package com.prabhupadalectures.common.downloads_api

import com.prabhupadalectures.common.utils.Lecture

data class DownloadsState(
    val lectures: List<Lecture> = emptyList(),
)
