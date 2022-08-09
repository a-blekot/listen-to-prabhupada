package com.listentoprabhupada.common.utils

sealed class ConnectionState {
    object Online : ConnectionState()
    object Offline : ConnectionState()
}