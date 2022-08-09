package com.listentoprabhupada.common.favorites_impl.store

import com.listentoprabhupada.common.data.Lecture

sealed interface FavoritesLabel {
    data class LecturesLoaded(val lectures: List<Lecture>) : FavoritesLabel
}