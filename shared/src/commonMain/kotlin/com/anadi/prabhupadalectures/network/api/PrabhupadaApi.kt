package com.anadi.prabhupadalectures.network.api

import com.anadi.prabhupadalectures.network.KtorClientFactory
import com.anadi.prabhupadalectures.network.Routes
import io.github.aakira.napier.Napier
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.math.roundToInt

typealias QueryParams = Map<String, Any>

private const val DEFAULT_BUFFER_SIZE = 4088L

const val DEFAULT_HTTP_TIMEOUT = 20_000L
const val DOWNLOAD_HTTP_TIMEOUT = 300_000L

interface PrabhupadaApi {
    suspend fun getResults(params: QueryParams): Result<ApiModel>
    suspend fun downloadFile(writeChannel: ByteWriteChannel, url: String): Flow<DownloadState>
}

class PrabhupadaApiImpl(private val client: HttpClient) : PrabhupadaApi {
    override suspend fun getResults(params: QueryParams): Result<ApiModel> =
        result {
            client.get(Routes.FILE) {
                params.forEach {
                    parameter(it.key, it.value)
                }
            }
        }

    override suspend fun downloadFile(writeChannel: ByteWriteChannel, url: String): Flow<DownloadState> {
        return flow {

            try {
                client.get<HttpStatement>(url) {
                    timeout {
                        requestTimeoutMillis = DOWNLOAD_HTTP_TIMEOUT
                        socketTimeoutMillis = DOWNLOAD_HTTP_TIMEOUT
                        connectTimeoutMillis = DOWNLOAD_HTTP_TIMEOUT
                    }
                }
                    .execute { httpResponse ->
                    emit(Progress(ZERO_PROGRESS))

                    val contentLength = httpResponse.contentLength()
                        ?: run {
                            emit(Error("httpResponse.contentLength() is null"))
                            return@execute
                        }

                    val channel: ByteReadChannel = httpResponse.receive()
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
