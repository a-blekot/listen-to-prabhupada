package com.listentoprabhupada.common.results_api

import com.arkivanov.decompose.value.Value
import com.listentoprabhupada.common.data.LectureComponent

interface ResultsComponent: LectureComponent {
    val flow: Value<ResultsState>

    fun onPage(page: Int) {}
    fun onCurrentLecture(id: Long, isPlaying: Boolean) {}
}