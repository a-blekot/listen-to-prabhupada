package com.listentoprabhupada.common.downloads_api

import com.listentoprabhupada.common.utils.Lecture

data class DownloadsState(
    val lectures: List<Lecture> = emptyList(),
)
