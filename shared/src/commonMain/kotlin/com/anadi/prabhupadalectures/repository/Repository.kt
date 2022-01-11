package com.anadi.prabhupadalectures.repository

import com.anadi.prabhupadalectures.network.api.ApiModel
import com.anadi.prabhupadalectures.network.api.PrabhupadaApi
import com.anadi.prabhupadalectures.network.api.QueryParams
import io.github.aakira.napier.Napier

interface Repository {
    suspend fun getResults(queryParams: QueryParams): Result<ApiModel>
}

class RepositoryImpl(private val prabhupadaApi: PrabhupadaApi): Repository {
    override suspend fun getResults(queryParams: QueryParams): Result<ApiModel> =
        result { prabhupadaApi.getResults(queryParams) }
}

private inline fun <T> result(block: () -> T) =
    try {
        Result.success(block())
    } catch (t: Throwable) {
        Napier.e("UserRepositoryImpl ", throwable = t)
        Result.failure(t)
    }