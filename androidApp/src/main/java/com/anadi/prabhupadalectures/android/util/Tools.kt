package com.anadi.prabhupadalectures.android.util

import android.app.ActivityManager
import android.content.Context

private fun Context.isMyServiceRunning(serviceClass: Class<*>) =
    try {
        (getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager)
            ?.getRunningServices(Int.MAX_VALUE)
            ?.any { it.service.className == serviceClass.name} ?: false
    } catch (e: Exception) {
        false
    }
