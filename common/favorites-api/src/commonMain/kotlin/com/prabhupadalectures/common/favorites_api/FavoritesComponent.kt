package com.listentoprabhupada.common.favorites_api

import com.arkivanov.decompose.value.Value

interface FavoritesComponent {

    val flow: Value<FavoritesState>

    fun onPlay(id: Long) {}
    fun onPause() {}
    fun onFavorite(id: Long, isFavorite: Boolean) {}
    fun onCurrentLecture(id: Long, isPlaying: Boolean) {}
}
