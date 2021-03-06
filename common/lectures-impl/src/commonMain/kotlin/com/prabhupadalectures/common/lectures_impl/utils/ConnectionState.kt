package com.prabhupadalectures.common.lectures_impl.utils

sealed class ConnectionState {
    object Online : ConnectionState()
    object Offline : ConnectionState()
}