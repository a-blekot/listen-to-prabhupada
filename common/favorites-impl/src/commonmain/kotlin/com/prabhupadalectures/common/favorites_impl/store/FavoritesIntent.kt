package com.prabhupadalectures.common.favorites_impl.store

sealed interface FavoritesIntent {
    data class CurrentLecture(val id: Long, val isPlaying: Boolean) : FavoritesIntent
    data class Favorite(val id: Long, val isFavorite: Boolean) : FavoritesIntent
}
