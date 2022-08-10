package com.listentoprabhupada.common.results_impl

import com.listentoprabhupada.common.database.Database
import com.listentoprabhupada.common.network_api.PrabhupadaApi
import com.listentoprabhupada.common.utils.dispatchers.DispatcherProvider

class LecturesDeps(
    val db: Database,
    val api: PrabhupadaApi,
    val dispatchers: DispatcherProvider,
)