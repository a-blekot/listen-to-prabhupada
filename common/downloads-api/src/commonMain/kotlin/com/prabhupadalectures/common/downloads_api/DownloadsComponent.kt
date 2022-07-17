package com.listentoprabhupada.common.downloads_api

import com.arkivanov.decompose.value.Value

interface DownloadsComponent {

    val flow: Value<DownloadsState>

    fun onPlay(id: Long) {}
    fun onPause() {}
    fun onRemove(id: Long) {}
    fun onFavorite(id: Long, isFavorite: Boolean) {}
    fun onCurrentLecture(id: Long, isPlaying: Boolean) {}
}
