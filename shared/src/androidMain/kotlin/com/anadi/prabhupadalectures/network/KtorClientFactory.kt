package com.anadi.prabhupadalectures.network

import com.anadi.prabhupadalectures.network.api.DEFAULT_HTTP_TIMEOUT
import io.github.aakira.napier.Napier
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
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
                        Napier.v(tag = "AndroidHttpClient", message = message)
                    }
                }
            }
            install(HttpTimeout) {
                requestTimeoutMillis = DEFAULT_HTTP_TIMEOUT
                socketTimeoutMillis = DEFAULT_HTTP_TIMEOUT
                connectTimeoutMillis = DEFAULT_HTTP_TIMEOUT
            }
//            install(CustomHeadersFeature) {
//                appId = APP_ID
//            }

            defaultRequest {
                header("app-id", APP_ID)
            }
        }
}
//
//class CustomHeadersFeature(private val appId: String) {
//    class Config(var appId: String = "")
//
//    companion object Feature : HttpClientFeature<Config, CustomHeadersFeature> {
//        override val key: AttributeKey<CustomHeadersFeature> =
//            AttributeKey("CustomHeadersFeature")
//
//        override fun install(feature: CustomHeadersFeature, scope: HttpClient) {
//            scope.feature(HttpSend)?.intercept { call, _ ->
//                if (call.response.status == HttpStatusCode.Unauthorized) {
//                    val request = HttpRequestBuilder().apply {
//                        header("app-id", feature.appId)
//                    }
//                    execute(request)
//                } else {
//                    call
//                }
//            }
//        }
//
//        override fun prepare(block: Config.() -> Unit): CustomHeadersFeature {
//            val config = Config().apply(block)
//            return CustomHeadersFeature(appId = config.appId)
//        }
//    }
//}
