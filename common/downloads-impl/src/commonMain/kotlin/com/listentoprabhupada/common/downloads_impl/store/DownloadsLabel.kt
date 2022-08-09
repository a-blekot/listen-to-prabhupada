package com.listentoprabhupada.common.downloads_impl.store

import com.listentoprabhupada.common.data.Lecture

sealed interface DownloadsLabel {
    data class LecturesLoaded(val lectures: List<Lecture>) : DownloadsLabel
}