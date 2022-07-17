package com.listentoprabhupada.common.network

import com.listentoprabhupada.common.network_api.*
import com.listentoprabhupada.common.utils.dispatchers.DispatcherProvider
import io.github.aakira.napier.Napier
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt
import kotlin.native.concurrent.SharedImmutable

private const val FILE_TYPE_AUDIO = 1

@SharedImmutable
private const val DEFAULT_BUFFER_SIZE = 4088L

@SharedImmutable
const val DEFAULT_HTTP_TIMEOUT = 20_000L

@SharedImmutable
const val DOWNLOAD_HTTP_TIMEOUT = 300_000L

class PrabhupadaApiImpl(
    private val client: HttpClient
) : PrabhupadaApi {
    override suspend fun getResults(params: QueryParams): Result<ApiModel> =
        result {
            client.get(Routes.FILE) {
                params.forEach {
                    parameter(it.key, it.value)
                }
                parameter(FILE_TYPE_QUERY_KEY, FILE_TYPE_AUDIO)
            }.body()
        }

    override suspend fun getResults(page: Int): ApiModel =
        client.get(Routes.FILE) {
            parameter(PAGE_QUERY_KEY, page)
            parameter(FILE_TYPE_QUERY_KEY, FILE_TYPE_AUDIO)
        }.body()

    override suspend fun downloadFile(writeChannel: ByteWriteChannel, url: String): Flow<DownloadState> {
        return flow {
            try {
                Napier.d("downloadFile $url", tag = "PrabhupadaApi")

                client.prepareGet(url) {
                    timeout {
                        requestTimeoutMillis = DOWNLOAD_HTTP_TIMEOUT
                        socketTimeoutMillis = DOWNLOAD_HTTP_TIMEOUT
                        connectTimeoutMillis = DOWNLOAD_HTTP_TIMEOUT
                    }
                }
                    .execute { httpResponse ->
                        emit(Progress(ZERO_PROGRESS.toInt()))

                        val contentLength = httpResponse.contentLength()
                            ?: run {
                                emit(Error("httpResponse.contentLength() is null"))
                                return@execute
                            }

                        val channel: ByteReadChannel = httpResponse.body()
                        while (!channel.isClosedForRead) {
                            val packet = channel.readRemaining(DEFAULT_BUFFER_SIZE)
                            while (!packet.isEmpty) {
                                val bytes = packet.readBytes()
                                writeChannel.writeAvailable(bytes)

                                val totalReceived = writeChannel.totalBytesWritten
                                emit(Progress((totalReceived * 100f / contentLength).roundToInt()))

                                println("Received $totalReceived bytes from $contentLength")
                            }
                        }

                        if (httpResponse.status.isSuccess())
                            emit(Success)
                        else
                            emit(Error("httpResponse.status = ${httpResponse.status.value}, ${httpResponse.status.description}"))
                    }
            } catch (t: Throwable) {
                emit(Error("error while downloading file", t))
            }
        }
    }
}

fun createPrabhupadaApi(): PrabhupadaApi =
    PrabhupadaApiImpl(KtorClientFactory().build())

private inline fun <T> result(block: () -> T) =
    try {
        Result.success(block())
    } catch (t: Throwable) {
        Napier.e("PrabhupadaApiImpl ", throwable = t)
        Result.failure(t)
    }
