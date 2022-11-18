package com.listentoprabhupada.common.utils.connectivity

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {

    fun observe(): Flow<Boolean>
    fun start() {}
    fun stop() {}
}