package com.listentoprabhupada.common.settings

import com.russhwolf.settings.Settings
import kotlin.native.concurrent.SharedImmutable

@SharedImmutable
private const val KEY_NOTIFICATION_ID = "KEY_NOTIFICATION_ID"
@SharedImmutable
const val DOWNLOAD_NOTIFICATION_ID = 16198

fun Settings.getNextNotificationId(): Int {
    val notificationId = getIntOrNull(KEY_NOTIFICATION_ID) ?: DOWNLOAD_NOTIFICATION_ID + 1
    putInt(KEY_NOTIFICATION_ID, notificationId + 1)
    return notificationId
}