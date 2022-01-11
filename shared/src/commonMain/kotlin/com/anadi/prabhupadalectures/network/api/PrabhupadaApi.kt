package com.anadi.prabhupadalectures.network.api

import com.anadi.prabhupadalectures.network.KtorClientFactory
import com.anadi.prabhupadalectures.network.Routes
import io.ktor.client.*
import io.ktor.client.request.*

typealias QueryParams = Map<String, Any>

interface PrabhupadaApi {
    suspend fun getResults(params: QueryParams): ApiModel
}

class PrabhupadaApiImpl(private val client: HttpClient) : PrabhupadaApi {
    override suspend fun getResults(params: QueryParams): ApiModel =
        client.get(Routes.FILE) {
            params.forEach {
                parameter(it.key, it.value)
            }
        }
}

fun createPrabhupadaApi(): PrabhupadaApi =
    PrabhupadaApiImpl(KtorClientFactory().build())