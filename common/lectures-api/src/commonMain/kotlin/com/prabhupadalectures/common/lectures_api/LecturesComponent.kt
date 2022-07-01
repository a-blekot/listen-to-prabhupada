package com.prabhupadalectures.common.lectures_api

import com.arkivanov.decompose.value.Value

interface LecturesComponent {

    val flow: Value<LecturesState>

    fun onPage(page: Int) {}
    fun onFavorite(id: Long, isFavorite: Boolean) {}
    fun onUpdateFilters() {}
    fun onCurrentLecture(id: Long, isPlaying: Boolean) {}

    fun onPause() {}
    fun onPlay(id: Long) {}
    fun onDownload(id: Long) {}
}