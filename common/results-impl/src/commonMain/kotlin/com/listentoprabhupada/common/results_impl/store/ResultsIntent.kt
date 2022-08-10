package com.listentoprabhupada.common.results_impl.store

sealed interface ResultsIntent {
    object UpdateLectures: ResultsIntent
    data class CurrentLecture(val id: Long, val isPlaying: Boolean) : ResultsIntent
    data class Favorite(val id: Long, val isFavorite: Boolean) : ResultsIntent
    data class UpdatePage(val page: Int) : ResultsIntent
}