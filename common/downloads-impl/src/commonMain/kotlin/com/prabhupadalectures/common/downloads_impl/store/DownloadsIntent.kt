package com.listentoprabhupada.common.downloads_impl.store

sealed interface DownloadsIntent {
    data class CurrentLecture(val id: Long, val isPlaying: Boolean) : DownloadsIntent
    data class Favorite(val id: Long, val isFavorite: Boolean) : DownloadsIntent
}
