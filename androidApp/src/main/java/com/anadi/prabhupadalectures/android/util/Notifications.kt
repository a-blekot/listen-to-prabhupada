package com.anadi.prabhupadalectures.android.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.annotation.StringRes

val notificationColor = Color.rgb(182, 93, 13)

val Context.notificationManager
    get() = getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager

fun Context.createNotificationChannel(channelId: String, @StringRes channelNameId: Int) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
        return
    }

    notificationManager?.run {
        val channel = NotificationChannel(
            channelId,
            getString(channelNameId),
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            lightColor = notificationColor
        }

        createNotificationChannel(channel)
    }
}