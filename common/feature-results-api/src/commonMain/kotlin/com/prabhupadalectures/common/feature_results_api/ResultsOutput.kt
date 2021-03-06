package com.prabhupadalectures.common.feature_results_api

sealed interface ResultsOutput {
    object EditFilters : ResultsOutput
    object ShowDownloads : ResultsOutput
    object ShowFavorites : ResultsOutput
    object ShowSettings : ResultsOutput
    data class Message(val text: String) : ResultsOutput
}
