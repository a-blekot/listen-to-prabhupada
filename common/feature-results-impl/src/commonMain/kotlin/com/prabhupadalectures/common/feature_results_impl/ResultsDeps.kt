package com.prabhupadalectures.common.feature_results_impl

import com.prabhupadalectures.common.database.Database
import com.prabhupadalectures.common.network_api.PrabhupadaApi
import com.prabhupadalectures.common.player_api.PlayerBus
import kotlin.coroutines.CoroutineContext

data class ResultsDeps(
    val db: Database,
    val api: PrabhupadaApi,
    val playerBus: PlayerBus,
    val ioContext: CoroutineContext,
    val mainContext: CoroutineContext,
)
