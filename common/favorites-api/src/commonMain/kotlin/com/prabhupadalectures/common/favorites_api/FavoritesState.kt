package com.prabhupadalectures.common.favorites_api

import com.prabhupadalectures.common.utils.Lecture

data class FavoritesState(
    val lectures: List<Lecture> = emptyList(),
)
