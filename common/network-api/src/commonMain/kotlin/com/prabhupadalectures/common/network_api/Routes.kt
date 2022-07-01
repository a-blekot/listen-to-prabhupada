package com.prabhupadalectures.common.network_api

import kotlin.native.concurrent.SharedImmutable
import kotlin.native.concurrent.ThreadLocal

@SharedImmutable
const val PAGE_QUERY_KEY = "page"

const val FILE_TYPE_QUERY_KEY = "t"

@ThreadLocal
object Routes {
    const val BASE_URL = "https://ydl3.da.net.ua"
    const val API = "$BASE_URL/api"
    const val FILE = "$API/v1/archive/file"
}
