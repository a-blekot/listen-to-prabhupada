package com.prabhupadalectures.lectures.mvi

import com.prabhupadalectures.common.database.Database
import com.prabhupadalectures.common.network_api.PrabhupadaApi

class Dependencies(
    val db: Database,
    val api: PrabhupadaApi
)