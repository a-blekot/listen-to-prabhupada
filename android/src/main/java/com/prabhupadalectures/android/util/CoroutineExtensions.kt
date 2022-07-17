package com.listentoprabhupada.android.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive

fun CoroutineScope.cancelSafely(msg: String) {
    try {
        if (isActive) {
            cancel(msg)
        }
    } catch (e: IllegalStateException) {

    }
}