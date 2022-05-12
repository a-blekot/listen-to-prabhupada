package com.prabhupadalectures.common.lectures_api

sealed interface LecturesOutput {
    object Pause : LecturesOutput
    object Next : LecturesOutput
    object Prev : LecturesOutput
    object SeekForward : LecturesOutput
    object SeekBack : LecturesOutput
    object SliderReleased : LecturesOutput

    data class Play(val lectureId: Long) : LecturesOutput
    data class SeekTo(val timeMs: Long) : LecturesOutput
    data class Download(val lecture: Lecture) : LecturesOutput
    data class UpdatePlaylist(val lectures: List<Lecture>) : LecturesOutput
}
