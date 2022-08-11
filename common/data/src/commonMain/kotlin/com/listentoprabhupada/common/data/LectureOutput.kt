package com.listentoprabhupada.common.data

interface LectureOutput {
    object Pause : LectureOutput
    data class Play(val lectureId: Long) : LectureOutput
    data class UpdatePlaylist(val lectures: List<Lecture>) : LectureOutput
}