package com.prabhupadalectures.common.favorites_impl

import com.prabhupadalectures.common.database.Database
import com.prabhupadalectures.common.network_api.PrabhupadaApi
import com.prabhupadalectures.common.utils.dispatchers.DispatcherProvider

data class FavoritesDeps(
    val db: Database,
    val api: PrabhupadaApi,
    val dispatchers: DispatcherProvider,
)
