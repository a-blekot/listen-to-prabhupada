package com.prabhupadalectures.common.network

import io.ktor.client.*

expect class KtorClientFactory() {
    fun build() : HttpClient
}