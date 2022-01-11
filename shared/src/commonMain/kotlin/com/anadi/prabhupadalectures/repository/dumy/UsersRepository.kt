package com.anadi.prabhupadalectures.repository.dumy

import com.anadi.prabhupadalectures.network.api.dumy.User
import com.anadi.prabhupadalectures.network.api.dumy.Users

interface UsersRepository {
    suspend fun getUsers(page: Int, limit: Int = 20): Result<Users>
    suspend fun getUser(id: Int): Result<User>
}