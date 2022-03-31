package com.anadi.prabhupadalectures.android.base.navigation

sealed class RouterEvent {
    object Pop : RouterEvent()
    class Push(val routes: Array<out Route>) : RouterEvent()
}
