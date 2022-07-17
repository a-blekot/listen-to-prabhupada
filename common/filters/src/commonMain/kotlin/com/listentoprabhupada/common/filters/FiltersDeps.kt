package com.listentoprabhupada.common.filters

import com.listentoprabhupada.common.database.Database
import com.listentoprabhupada.common.network_api.PrabhupadaApi
import com.listentoprabhupada.common.utils.dispatchers.DispatcherProvider
import kotlin.coroutines.CoroutineContext

data class FiltersDeps(
    val db: Database,
    val api: PrabhupadaApi,
    val dispatchers: DispatcherProvider
)
