package com.listentoprabhupada.common.network

import io.ktor.client.*

expect class KtorClientFactory() {
    fun build() : HttpClient
}