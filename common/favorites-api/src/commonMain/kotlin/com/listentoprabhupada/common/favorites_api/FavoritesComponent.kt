package com.listentoprabhupada.common.favorites_api

import com.arkivanov.decompose.value.Value
import com.listentoprabhupada.common.data.LectureComponent

interface FavoritesComponent: LectureComponent {
    val flow: Value<FavoritesState>
}
