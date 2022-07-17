package com.listentoprabhupada.common.feature_downloads_api

import com.listentoprabhupada.common.downloads_api.DownloadsComponent
import com.listentoprabhupada.common.player_api.PlayerComponent


interface DownloadsFeatureComponent {

    val downloadsComponent: DownloadsComponent
    val playerComponent: PlayerComponent

    fun onShowSettings() {}
}
