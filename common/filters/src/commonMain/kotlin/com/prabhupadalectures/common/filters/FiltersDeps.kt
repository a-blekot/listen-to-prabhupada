package com.prabhupadalectures.common.filters

import com.prabhupadalectures.common.database.Database
import com.prabhupadalectures.common.network_api.PrabhupadaApi
import com.prabhupadalectures.common.utils.dispatchers.DispatcherProvider
import kotlin.coroutines.CoroutineContext

data class FiltersDeps(
    val db: Database,
    val api: PrabhupadaApi,
    val dispatchers: DispatcherProvider
)
