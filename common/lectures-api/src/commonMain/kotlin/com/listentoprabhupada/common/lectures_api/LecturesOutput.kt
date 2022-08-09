package com.listentoprabhupada.common.lectures_api

import com.listentoprabhupada.common.data.Lecture

sealed interface LecturesOutput {
    object Pause : LecturesOutput
    data class Play(val lectureId: Long) : LecturesOutput
    data class UpdatePlaylist(val lectures: List<Lecture>) : LecturesOutput
}
