package com.listentoprabhupada.common.data

interface LectureComponent {
    fun onPause() {}
    fun onPlay(id: Long) {}
    fun onFavorite(id: Long, isFavorite: Boolean) {}
    fun onCurrentLecture(id: Long, isPlaying: Boolean) {}
}