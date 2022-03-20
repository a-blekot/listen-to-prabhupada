package com.anadi.prabhupadalectures.android.navigation

sealed class RouterEvent {
    object Pop : RouterEvent()
    class Push(val routes: Array<out Route>) : RouterEvent()
}
