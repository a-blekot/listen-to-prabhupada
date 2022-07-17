package com.listentoprabhupada.common.favorites_api

import com.listentoprabhupada.common.utils.Lecture

data class FavoritesState(
    val lectures: List<Lecture> = emptyList(),
)
