package com.anadi.prabhupadalectures.repository

import com.anadi.prabhupadalectures.network.api.User
import com.anadi.prabhupadalectures.network.api.Users

interface UsersRepository {
    suspend fun getUsers(page: Int, limit: Int = 20): Result<Users>
    suspend fun getUser(id: Int): Result<User>
}