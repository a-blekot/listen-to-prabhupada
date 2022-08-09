package com.listentoprabhupada.common.feature_downloads_api

sealed interface DownloadsFeatureOutput {
    object ShowSettings : DownloadsFeatureOutput
    data class Message(val text: String) : DownloadsFeatureOutput
}
