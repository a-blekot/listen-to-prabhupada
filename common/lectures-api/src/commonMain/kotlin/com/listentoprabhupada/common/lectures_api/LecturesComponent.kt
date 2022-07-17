package com.listentoprabhupada.common.lectures_api

import com.arkivanov.decompose.value.Value

interface LecturesComponent {

    val flow: Value<LecturesState>

    fun onPage(page: Int) {}
    fun onFavorite(id: Long, isFavorite: Boolean) {}
    fun onUpdateLectures() {}
    fun onCurrentLecture(id: Long, isPlaying: Boolean) {}

    fun onPause() {}
    fun onPlay(id: Long) {}
}