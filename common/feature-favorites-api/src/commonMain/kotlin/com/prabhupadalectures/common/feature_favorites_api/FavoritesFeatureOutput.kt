package com.listentoprabhupada.common.feature_favorites_api

sealed interface FavoritesFeatureOutput {
    object ShowSettings : FavoritesFeatureOutput
    data class Message(val text: String) : FavoritesFeatureOutput
}
