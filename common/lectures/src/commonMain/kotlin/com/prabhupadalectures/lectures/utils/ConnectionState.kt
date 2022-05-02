package com.prabhupadalectures.lectures.utils

sealed class ConnectionState {
    object Online : ConnectionState()
    object Offline : ConnectionState()
}