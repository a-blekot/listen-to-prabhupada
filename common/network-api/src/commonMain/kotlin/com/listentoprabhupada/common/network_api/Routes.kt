package com.listentoprabhupada.common.network_api

const val PAGE_QUERY_KEY = "page"
const val SEARCH_QUERY_KEY = "q"
const val FILE_TYPE_QUERY_KEY = "t"

object Routes {
    const val BASE_URL = "https://ydl3.da.net.ua"
    const val API = "$BASE_URL/api"
    const val FILE = "$API/v1/archive/file"
}
