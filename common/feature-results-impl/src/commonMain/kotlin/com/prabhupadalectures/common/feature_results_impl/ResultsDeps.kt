package com.listentoprabhupada.common.feature_results_impl

import com.listentoprabhupada.common.database.Database
import com.listentoprabhupada.common.network_api.PrabhupadaApi
import com.listentoprabhupada.common.player_api.PlayerBus
import com.listentoprabhupada.common.utils.dispatchers.DispatcherProvider
import kotlin.coroutines.CoroutineContext

data class ResultsDeps(
    val db: Database,
    val api: PrabhupadaApi,
    val playerBus: PlayerBus,
    val dispatchers: DispatcherProvider,
)
