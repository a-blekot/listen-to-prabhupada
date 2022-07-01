package com.prabhupadalectures.common.downloads_api

import com.prabhupadalectures.common.utils.Lecture

sealed interface DownloadsOutput {
    object Pause : DownloadsOutput
    data class Play(val lectureId: Long) : DownloadsOutput
    data class UpdatePlaylist(val lectures: List<Lecture>) : DownloadsOutput
}
