package com.prabhupadalectures.common.feature_downloads_api

import com.prabhupadalectures.common.downloads_api.DownloadsComponent
import com.prabhupadalectures.common.player_api.PlayerComponent


interface DownloadsFeatureComponent {

    val downloadsComponent: DownloadsComponent
    val playerComponent: PlayerComponent

    fun onShowSettings() {}
}
