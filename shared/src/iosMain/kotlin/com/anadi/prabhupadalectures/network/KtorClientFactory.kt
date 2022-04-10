package com.anadi.prabhupadalectures.network

import com.anadi.prabhupadalectures.network.api.DEFAULT_HTTP_TIMEOUT
import io.github.aakira.napier.Napier
import io.ktor.client.*
import io.ktor.client.engine.darwin.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*

actual class KtorClientFactory {
    actual fun build() =
        HttpClient(Darwin) {
            engine {
                configureRequest {
                    setAllowsCellularAccess(true)
                }
            }
            install(ContentNegotiation) {
                json()
            }
            install(Logging) {
                level = LogLevel.ALL

                logger = object : Logger {
                    override fun log(message: String) {
                        Napier.v(tag = "AndroidHttpClient", message = message)
                    }
                }
            }
            install(HttpTimeout) {
                requestTimeoutMillis = DEFAULT_HTTP_TIMEOUT
                socketTimeoutMillis = DEFAULT_HTTP_TIMEOUT
                connectTimeoutMillis = DEFAULT_HTTP_TIMEOUT
            }

            defaultRequest {
                header("app-id", APP_ID)
            }
        }
}