package com.listentoprabhupada.common.results_impl

import com.listentoprabhupada.common.database.Database
import com.listentoprabhupada.common.network_api.PrabhupadaApi
import com.listentoprabhupada.common.utils.RemoteConfig
import com.listentoprabhupada.common.utils.dispatchers.DispatcherProvider

class ResultsDeps(
    val db: Database,
    val api: PrabhupadaApi,
    val remoteConfig: RemoteConfig,
    val dispatchers: DispatcherProvider,
)