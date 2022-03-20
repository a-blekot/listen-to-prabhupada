package com.anadi.prabhupadalectures.repository

import com.russhwolf.settings.Settings

val settings = Settings()

private const val FILTERS_SEPARATOR = ", "
private const val KEY_VALUE_SEPARATOR = "::"
private const val KEY_FILTERS = "KEY_FILTERS"
private const val KEY_NOTIFICATION_ID = "KEY_NOTIFICATION_ID"

const val DOWNLOAD_NOTIFICATION_ID = 16198

fun Settings.saveFilters(filters: HashMap<String, Any>) {
    val result = filters.entries.joinToString(separator = FILTERS_SEPARATOR) {
        "${it.key}$KEY_VALUE_SEPARATOR${it.value}"
    }
    putString(KEY_FILTERS, result)
}

fun Settings.getFilters(): HashMap<String, Any> {
    val result = HashMap<String, Any>()

    getStringOrNull(KEY_FILTERS)
        ?.split(FILTERS_SEPARATOR)
        ?.forEach { filter ->
            val (name, value) = filter.split(KEY_VALUE_SEPARATOR)
            result[name] = value
        }

    return result
}

fun Settings.getNextNotificationId(): Int {
    val notificationId = getIntOrNull(KEY_NOTIFICATION_ID) ?: DOWNLOAD_NOTIFICATION_ID + 1
    putInt(KEY_NOTIFICATION_ID, notificationId + 1)
    return notificationId
}
