package com.anadi.prabhupadalectures.utils

sealed class ConnectionState {
    object Online : ConnectionState()
    object Offline : ConnectionState()
}