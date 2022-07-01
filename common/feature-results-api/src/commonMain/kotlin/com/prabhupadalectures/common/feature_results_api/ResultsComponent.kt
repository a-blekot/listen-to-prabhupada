package com.prabhupadalectures.common.feature_results_api

import com.prabhupadalectures.common.lectures_api.LecturesComponent
import com.prabhupadalectures.common.player_api.PlayerComponent

interface ResultsComponent {

    val lecturesComponent: LecturesComponent
    val playerComponent: PlayerComponent

    fun onEditFilters() {}
    fun onShowDownloads() {}
    fun onShowFavorites() {}
    fun onShowSettings() {}
}
