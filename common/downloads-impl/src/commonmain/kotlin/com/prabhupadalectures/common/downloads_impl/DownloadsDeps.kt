package com.prabhupadalectures.common.downloads_impl

import com.prabhupadalectures.common.database.Database
import com.prabhupadalectures.common.network_api.PrabhupadaApi
import com.prabhupadalectures.common.utils.dispatchers.DispatcherProvider

data class DownloadsDeps(
    val db: Database,
    val api: PrabhupadaApi,
    val dispatchers: DispatcherProvider,
)
