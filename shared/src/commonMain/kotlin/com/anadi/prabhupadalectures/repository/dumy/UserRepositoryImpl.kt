package com.anadi.prabhupadalectures.repository.dumy

import com.anadi.prabhupadalectures.network.api.dumy.UserApi
import io.github.aakira.napier.Napier

class UserRepositoryImpl(private val userApi: UserApi): UsersRepository {
    override suspend fun getUsers(page: Int, limit: Int) =
        result { userApi.getUsers(page, limit) }

    override suspend fun getUser(id: Int) =
        result { userApi.getUser(id) }
}

private inline fun <T> result(block: () -> T) =
    try {
        Result.success(block())
    } catch (t: Throwable) {
        Napier.e("UserRepositoryImpl ", throwable = t)
        Result.failure(t)
    }