package com.prabhupadalectures.common.lectures_api

import com.arkivanov.decompose.value.Value

interface LecturesComponent {

    val flow: Value<LecturesState>

    fun onPage(page: Int)
    fun onFavorite(id: Long, isFavorite: Boolean)
    fun onUpdateFilters()
    fun onCurrentLecture(id: Long, isPlaying: Boolean)

    fun onPause()
    fun onNext()
    fun onPrev()
    fun onSeekForward()
    fun onSeekBack()
    fun onSliderReleased()
    fun onPlay(id: Long)
    fun onSeekTo(timeMs: Long)
    fun onDownload(id: Long)
}