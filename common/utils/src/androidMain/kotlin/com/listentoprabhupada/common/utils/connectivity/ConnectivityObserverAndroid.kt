package com.listentoprabhupada.common.utils.connectivity

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class ConnectivityObserverAndroid(context: Context) : ConnectivityObserver {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override fun start() {
    }

    override fun stop() {
    }

    override fun observe(): Flow<Boolean> {
        return callbackFlow {

            val networkRequest = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build()

            val callback = networkCallback {
                launch {
                    send(it)
                }
            }

            connectivityManager.registerNetworkCallback(networkRequest, callback)

            val currentState = getCurrentConnectivityState(connectivityManager)
            trySend(currentState)

            awaitClose {
                connectivityManager.unregisterNetworkCallback(callback)
            }
        }.distinctUntilChanged()
    }

    private fun networkCallback(callback: (Boolean) -> Unit): ConnectivityManager.NetworkCallback =
        object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) = callback.invoke(true)
            override fun onLosing(network: Network, maxMsToLive: Int) = callback.invoke(true)
            override fun onLost(network: Network) = callback.invoke(false)
            override fun onUnavailable() = callback.invoke(false)
        }

    private fun getCurrentConnectivityState(connectivityManager: ConnectivityManager) =
        connectivityManager.allNetworks.any { network ->
            connectivityManager.getNetworkCapabilities(network)
                ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                ?: false
        }
}
