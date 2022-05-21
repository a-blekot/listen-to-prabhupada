package com.prabhupadalectures.common.utils

//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.SupervisorJob
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.launch
//
//fun <T> Flow<T>.collect(onEach: (T) -> Unit, onCompletion: (cause: Throwable?) -> Unit): Cancellable {
//    val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
//
//    scope.launch {
//        try {
//            collect {
//                onEach(it)
//            }
//
//            onCompletion(null)
//        } catch (e: Throwable) {
//            onCompletion(e)
//        }
//    }
//
//    return object : Cancellable {
//        override fun cancel() {
//            scope.cancel()
//        }
//    }
//}