package com.listentoprabhupada.common.feature_results_api

import com.listentoprabhupada.common.results_api.ResultsComponent
import com.listentoprabhupada.common.player_api.PlayerComponent

interface ResultsFeatureComponent {
    val resultsComponent: ResultsComponent
    val playerComponent: PlayerComponent
}
