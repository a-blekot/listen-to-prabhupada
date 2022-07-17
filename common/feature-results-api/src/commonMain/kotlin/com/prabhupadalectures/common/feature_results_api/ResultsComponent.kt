package com.listentoprabhupada.common.feature_results_api

import com.listentoprabhupada.common.lectures_api.LecturesComponent
import com.listentoprabhupada.common.player_api.PlayerComponent

interface ResultsComponent {

    val lecturesComponent: LecturesComponent
    val playerComponent: PlayerComponent

    fun onEditFilters() {}
    fun onShowDownloads() {}
    fun onShowFavorites() {}
    fun onShowSettings() {}
}
