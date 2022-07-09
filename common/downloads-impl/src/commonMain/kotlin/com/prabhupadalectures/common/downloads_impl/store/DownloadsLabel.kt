package com.prabhupadalectures.common.downloads_impl.store

import com.prabhupadalectures.common.utils.Lecture

sealed interface DownloadsLabel {
    data class LecturesLoaded(val lectures: List<Lecture>) : DownloadsLabel
}