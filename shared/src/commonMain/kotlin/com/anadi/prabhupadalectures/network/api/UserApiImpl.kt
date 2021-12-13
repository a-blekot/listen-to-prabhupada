package com.anadi.prabhupadalectures.network.api

import com.anadi.prabhupadalectures.network.Routes
import io.github.aakira.napier.Napier
import io.ktor.client.*
import io.ktor.client.request.*

class UserApiImpl(private val client: HttpClient) : UserApi {
    override suspend fun getUsers(page: Int, limit: Int): Users =
        client.get {
            url(Routes.USER)
            parameter("page", page)
            parameter("limit", limit)
        }

    override suspend fun getUser(id: Int): User =
        client.get {
            url(Routes.USER)
            parameter("id", id)
        }
}