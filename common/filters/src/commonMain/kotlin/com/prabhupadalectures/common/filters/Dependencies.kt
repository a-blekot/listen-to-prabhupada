package com.prabhupadalectures.common.filters

import com.prabhupadalectures.common.database.Database
import com.prabhupadalectures.common.network_api.PrabhupadaApi
import kotlin.coroutines.CoroutineContext

data class Dependencies(
    val db: Database,
    val api: PrabhupadaApi,
    val ioContext: CoroutineContext,
    val mainContext: CoroutineContext,
)
