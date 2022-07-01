package com.prabhupadalectures.common.feature_favorites_api

import com.prabhupadalectures.common.favorites_api.FavoritesComponent
import com.prabhupadalectures.common.player_api.PlayerComponent


interface FavoritesFeatureComponent {

    val favoritesComponent: FavoritesComponent
    val playerComponent: PlayerComponent

    fun onShowSettings() {}
}
