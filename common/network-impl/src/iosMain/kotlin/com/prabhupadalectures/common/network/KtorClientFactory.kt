package com.prabhupadalectures.common.network

import io.github.aakira.napier.Napier
import io.ktor.client.*
import io.ktor.client.engine.darwin.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

actual class KtorClientFactory {
    actual fun build() =
        HttpClient(Darwin) {
            engine {
                configureRequest {
                    setAllowsCellularAccess(true)
                }
            }
            install(ContentNegotiation) {
                json(Json{ ignoreUnknownKeys = true })
            }
            install(Logging) {
                level = LogLevel.ALL
                logger = object : Logger {
                    override fun log(message: String) {
                        Napier.v(tag = "DarwinHttpClient", message = message)
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