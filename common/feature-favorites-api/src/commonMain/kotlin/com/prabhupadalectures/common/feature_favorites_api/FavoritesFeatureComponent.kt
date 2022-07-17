package com.listentoprabhupada.common.feature_favorites_api

import com.listentoprabhupada.common.favorites_api.FavoritesComponent
import com.listentoprabhupada.common.player_api.PlayerComponent


interface FavoritesFeatureComponent {

    val favoritesComponent: FavoritesComponent
    val playerComponent: PlayerComponent

    fun onShowSettings() {}
}
