package com.listentoprabhupada.common.utils.connectivity

//import platform.posix.send

//
//import kotlinx.coroutines.flow.MutableStateFlow
//import cocoapods.Reachability.*
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//import platform.Foundation.NSLog
//import platform.darwin.dispatch_async
//import platform.darwin.dispatch_get_main_queue
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

class ConnectivityObserverIOS : ConnectivityObserver {

    override fun observe() =
        callbackFlow {
            launch {
                send(true)
            }
        }

//    private val connectivityStatus: ConnectivityStatus = ConnectivityStatus()
//    override fun observe() = connectivityStatus.isNetworkConnected
//
//    override fun start() =
//        connectivityStatus.start()
//
//    override fun stop() =
//        connectivityStatus.stop()
//
//    fun getStatus(success: (Boolean) -> Unit) = connectivityStatus.getStatus(success)


}
//
//private class ConnectivityStatus {
//    val isNetworkConnected = MutableStateFlow(false)
//
//    private var reachability: Reachability? = null
//
//    fun start() {
//        dispatch_async(dispatch_get_main_queue()) {
//            reachability = Reachability.reachabilityForInternetConnection()
//
//            val reachableCallback = { _: Reachability? ->
//                dispatch_async(dispatch_get_main_queue()) {
//                    NSLog("Connected")
//
//                    isNetworkConnected.value = true
//                }
//            }
//            reachability?.reachableBlock = reachableCallback
//
//            val unreachableCallback = { _: Reachability? ->
//                dispatch_async(dispatch_get_main_queue()) {
//                    NSLog("Disconnected")
//
//                    isNetworkConnected.value = false
//                }
//            }
//            reachability?.unreachableBlock = unreachableCallback
//
//            reachability?.startNotifier()
//
//            dispatch_async(dispatch_get_main_queue()) {
//                isNetworkConnected.value = reachability?.isReachable() ?: false
//
//                NSLog("Initial reachability: ${reachability?.isReachable()}")
//            }
//        }
//    }
//
//    fun stop() {
//        reachability?.stopNotifier()
//    }
//
//    fun getStatus(success: (Boolean) -> Unit) {
//        CoroutineScope(Dispatchers.Default).launch {
//            isNetworkConnected.collect { status ->
//                withContext(Dispatchers.Main) {
//                    success(status)
//                }
//            }
//        }
//    }
//}