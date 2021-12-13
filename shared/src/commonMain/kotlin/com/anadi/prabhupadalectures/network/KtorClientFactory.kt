package com.anadi.prabhupadalectures.network

import io.ktor.client.*

expect class KtorClientFactory() {
    fun build() : HttpClient
}