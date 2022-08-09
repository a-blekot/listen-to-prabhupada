package com.listentoprabhupada.common.favorites_api

import com.listentoprabhupada.common.data.Lecture

sealed interface FavoritesOutput {
    object Pause : FavoritesOutput
    data class Play(val lectureId: Long) : FavoritesOutput
    data class UpdatePlaylist(val lectures: List<Lecture>) : FavoritesOutput
}
