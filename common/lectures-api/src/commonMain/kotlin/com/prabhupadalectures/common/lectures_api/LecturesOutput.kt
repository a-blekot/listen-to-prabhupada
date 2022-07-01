package com.prabhupadalectures.common.lectures_api

import com.prabhupadalectures.common.utils.Lecture

sealed interface LecturesOutput {
    object Pause : LecturesOutput
    data class Play(val lectureId: Long) : LecturesOutput
    data class Download(val lecture: Lecture) : LecturesOutput
    data class UpdatePlaylist(val lectures: List<Lecture>) : LecturesOutput
}
