package com.listentoprabhupada.android.util

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.listentoprabhupada.common.utils.analytics.Analytics
import com.listentoprabhupada.common.utils.analytics.AnalyticsEvent
import io.github.aakira.napier.Napier

class AnalyticsAndroid(private val context: Context): Analytics {
    private val analytics by lazy { FirebaseAnalytics.getInstance(context) }

    override fun logEvent(event: AnalyticsEvent, params: Map<String, Any>?) {
        val bundle = Bundle()

        params?.forEach { entry ->
            when (val value = entry.value) {
                is Int -> bundle.putInt(entry.key, value)
                is Long -> bundle.putLong(entry.key, value)
                is Boolean -> bundle.putBoolean(entry.key, value)
                is String -> bundle.putString(entry.key, value)
            }
        }
        
        analytics.logEvent(event.title, bundle.nullIfEmpty())
    }
}

class AnalyticsAndroidDebug: Analytics {
    override fun logEvent(event: AnalyticsEvent, params: Map<String, Any>?) =
        Napier.d("${event.title}: $params", tag = "ANALYTICS")
}

private fun Bundle.nullIfEmpty() = if (isEmpty) null else this
