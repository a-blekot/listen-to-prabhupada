package com.listentoprabhupada.android.util

import android.content.Context
//import com.google.firebase.FirebaseApp
//import com.google.firebase.crashlytics.FirebaseCrashlytics
import io.github.aakira.napier.Antilog
import io.github.aakira.napier.LogLevel

class CrashlyticsAntilog(context: Context) : Antilog() {

//    init {
//        FirebaseApp.initializeApp(context)
//    }
//
//    private val crashlytics by lazy { FirebaseCrashlytics.getInstance() }

    override fun performLog(priority: LogLevel, tag: String?, throwable: Throwable?, message: String?) {
        if (priority == LogLevel.ERROR || priority == LogLevel.DEBUG) {
            if (message != null) {
//                when (tag) {
//                    null -> crashlytics.log(message)
//                    else -> crashlytics.setCustomKey(tag, message)
//                }
            }
            if (throwable != null) {
//                crashlytics.recordException(throwable)
            }
        }
    }
}