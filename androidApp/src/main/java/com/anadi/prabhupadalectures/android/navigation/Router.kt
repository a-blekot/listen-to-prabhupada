package com.anadi.prabhupadalectures.android.navigation

import io.github.aakira.napier.Napier
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject
import javax.inject.Singleton

interface Router {
    fun pop(): Boolean
    fun push(vararg routes: Route): Boolean
    val routerEvents: Flow<RouterEvent>
}

@Singleton
class RouterImpl @Inject constructor() : Router {
    private val _routerEvents = Channel<RouterEvent>()
    override val routerEvents = _routerEvents.receiveAsFlow()

    override fun pop(): Boolean {
        Napier.e("RouterImpl: goBack")
        return _routerEvents.trySend(RouterEvent.Pop).isSuccess
    }

    override fun push(vararg routes: Route): Boolean {
        Napier.e("RouterImpl: goTo")
        return _routerEvents.trySend(RouterEvent.Push(routes)).isSuccess
    }
}
