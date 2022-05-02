package com.prabhupadalectures.common.network

import io.github.aakira.napier.Napier
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*

actual class KtorClientFactory {
    actual fun build() =
        HttpClient(OkHttp) {
            install(ContentNegotiation) {
                json()
            }
            install(Logging) {
                level = LogLevel.ALL
                logger = object : Logger {
                    override fun log(message: String) {
                        Napier.v(tag = "OkHttpClient", message = message)
                    }
                }
            }
            install(HttpTimeout) {
                requestTimeoutMillis = DEFAULT_HTTP_TIMEOUT
                socketTimeoutMillis = DEFAULT_HTTP_TIMEOUT
                connectTimeoutMillis = DEFAULT_HTTP_TIMEOUT
            }
        }
}
