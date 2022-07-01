package com.prabhupadalectures.common.favorites_impl.store

import com.prabhupadalectures.common.utils.Lecture

sealed interface FavoritesLabel {
    data class LecturesLoaded(val lectures: List<Lecture>) : FavoritesLabel
}