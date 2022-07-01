package com.prabhupadalectures.common.favorites_api

import com.prabhupadalectures.common.utils.Lecture

sealed interface FavoritesOutput {
    object Pause : FavoritesOutput
    data class Play(val lectureId: Long) : FavoritesOutput
    data class Download(val lecture: Lecture) : FavoritesOutput
    data class UpdatePlaylist(val lectures: List<Lecture>) : FavoritesOutput
}
