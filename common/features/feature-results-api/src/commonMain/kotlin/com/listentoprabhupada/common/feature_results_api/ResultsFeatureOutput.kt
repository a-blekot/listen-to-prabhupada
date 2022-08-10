package com.listentoprabhupada.common.feature_results_api

sealed interface ResultsFeatureOutput {
    data class Message(val text: String) : ResultsFeatureOutput
}
