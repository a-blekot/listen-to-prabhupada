package com.prabhupadalectures.common.network_api

import io.ktor.utils.io.*
import kotlinx.coroutines.flow.Flow

typealias QueryParams = Map<String, Any>

interface PrabhupadaApi {
    suspend fun getResults(params: QueryParams): Result<ApiModel>
    suspend fun getResults(page: Int): ApiModel
    suspend fun downloadFile(writeChannel: ByteWriteChannel, url: String): Flow<DownloadState>
}