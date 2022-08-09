package com.listentoprabhupada.common.downloads_api

import com.listentoprabhupada.common.data.Lecture

data class DownloadsState(
    val lectures: List<Lecture> = emptyList(),
)
