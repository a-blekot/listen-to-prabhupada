package com.listentoprabhupada.common.downloads_impl

import com.listentoprabhupada.common.database.Database
import com.listentoprabhupada.common.network_api.PrabhupadaApi
import com.listentoprabhupada.common.utils.dispatchers.DispatcherProvider

data class DownloadsDeps(
    val db: Database,
    val api: PrabhupadaApi,
    val dispatchers: DispatcherProvider,
)
