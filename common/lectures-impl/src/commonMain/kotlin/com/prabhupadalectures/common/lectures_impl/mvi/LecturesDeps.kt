package com.prabhupadalectures.common.lectures_impl.mvi

import com.prabhupadalectures.common.database.Database
import com.prabhupadalectures.common.network_api.PrabhupadaApi
import kotlin.coroutines.CoroutineContext

class LecturesDeps(
    val db: Database,
    val api: PrabhupadaApi,
    val mainContext: CoroutineContext,
    val ioContext: CoroutineContext,
)