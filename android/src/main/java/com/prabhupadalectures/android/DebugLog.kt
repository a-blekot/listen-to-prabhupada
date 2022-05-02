package com.prabhupadalectures.android

import android.util.Log

object DebugLog {
    fun d(tag: String = "DebugLog", msg: String) {
        if (BuildConfig.DEBUG) Log.d(tag, msg)
    }

    fun w(tag: String = "DebugLog", msg: String) {
        if (BuildConfig.DEBUG) Log.w(tag, msg)
    }

    fun e(tag: String = "DebugLog", msg: String? = null, e: Throwable? = null) {
        if (BuildConfig.DEBUG) Log.e(tag, msg, e)
    }
}