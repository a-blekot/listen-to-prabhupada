package com.listentoprabhupada.common.favorites_impl.store

import com.listentoprabhupada.common.utils.Lecture

sealed interface FavoritesLabel {
    data class LecturesLoaded(val lectures: List<Lecture>) : FavoritesLabel
}