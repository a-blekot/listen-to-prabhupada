package com.anadi.prabhupadalectures.datamodel

import com.anadi.prabhupadalectures.network.Routes
import com.anadi.prabhupadalectures.network.api.UserPreview
import com.anadi.prabhupadalectures.network.api.Users
import com.anadi.prabhupadalectures.repository.UsersRepository
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class UserState(
    val loading: Boolean,
    val users: List<UserPreview>
)

data class Pagination(
    val total: Int = 0,
    val page: Int = 0,
    val limit: Int = 20
) {
    constructor(users: Users) : this(
        total = users.total,
        page = users.page,
        limit = users.limit,
    )

    fun next() = copy(page = page + 1)
}

class UserDataModel(private val usersRepository: UsersRepository, withLog: Boolean) :
    CoroutineScope by CoroutineScope(Dispatchers.Main) {

    private val state = MutableStateFlow(UserState(false, emptyList()))
    private var pagination = Pagination()

    init {
        if (withLog) Napier.base(DebugAntilog())
    }

    fun observeState(): StateFlow<UserState> = state

    suspend fun loadMore() {
        setLoading(true)

        pagination.run {
            Napier.d("loadMore: page = $page, limit = $limit")
            val users = usersRepository.getUsers(page, limit)
            if (users.isSuccess) {
                users.getOrNull()?.let {
                    updatePagination(it)
                    addUsers(it.data)
                }
            } else {
                setLoading(false)
            }
        }

    }

    private fun setLoading(loading: Boolean) {
        val oldState = state.value
        val newState = oldState.copy(loading = loading)

        if (newState != oldState) {
            Napier.d(tag = "UserDataModel", message = "NewState: $newState")
            state.value = newState
        }
    }

    private fun updatePagination(users: Users) {
        pagination = Pagination(users).next()
    }

    private fun addUsers(newUsers: List<UserPreview>) {
        val oldState = state.value

        val newList = oldState.users.toMutableList().apply {
            addAll(newUsers)
        }

        state.value = oldState.copy(users = newList)
    }
}