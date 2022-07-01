package com.prabhupadalectures.common.feature_downloads_impl

import com.prabhupadalectures.common.database.Database
import com.prabhupadalectures.common.network_api.PrabhupadaApi
import com.prabhupadalectures.common.player_api.PlayerBus
import com.prabhupadalectures.common.utils.dispatchers.DispatcherProvider

data class DownloadsFeatureDeps(
    val db: Database,
    val api: PrabhupadaApi,
    val playerBus: PlayerBus,
    val dispatchers: DispatcherProvider,
)
