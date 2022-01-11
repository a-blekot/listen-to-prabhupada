package com.anadi.prabhupadalectures.network

import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.util.*

actual class KtorClientFactory {
    actual fun build() =
        HttpClient(Android) {
            install(JsonFeature) {
                serializer = KotlinxSerializer(
                    kotlinx.serialization.json.Json {
                        ignoreUnknownKeys = true
//                        coerceInputValues = true
                    }
                )
            }
            install(Logging) {
                logger = Logger.ANDROID
                level = LogLevel.ALL
            }
//            install(CustomHeadersFeature) {
//                appId = APP_ID
//            }

            defaultRequest {
                header("app-id", APP_ID)
            }
        }
}

class CustomHeadersFeature(private val appId: String) {
    class Config(var appId: String = "")

    companion object Feature : HttpClientFeature<Config, CustomHeadersFeature> {
        override val key: AttributeKey<CustomHeadersFeature> =
            AttributeKey("CustomHeadersFeature")

        override fun install(feature: CustomHeadersFeature, scope: HttpClient) {
            scope.feature(HttpSend)?.intercept { call, _ ->
                if (call.response.status == HttpStatusCode.Unauthorized){
                    val request = HttpRequestBuilder().apply {
                        header("app-id", feature.appId)
                    }
                    execute(request)
                } else {
                    call
                }
            }
        }

        override fun prepare(block: Config.() -> Unit): CustomHeadersFeature {
            val config = Config().apply(block)
            return CustomHeadersFeature(appId = config.appId)
        }
    }
}
