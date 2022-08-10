package com.listentoprabhupada.android_ui.utils

enum class TimeFormat(val format: String) {
    MINUTES("%02d:%02d"),
    MINUTES_SUFFIX("%02dm:%02ds"),
    HOURS("%02d:%02d"),
    HOURS_SUFFIX("%02dh:%02dm"),
    DAYS("%02dd:%02dh"),
    HOURS_MINUTES_SECONDS("%d:%02d:%02d"),
    DIALOGS_TIMER("%02d:%02d:%02d");
}

fun getTimeFormatMinutes(showSuffix: Boolean) =
    (if (showSuffix) TimeFormat.MINUTES_SUFFIX else TimeFormat.MINUTES).format

fun getTimeFormatHours(showSuffix: Boolean) =
    (if (showSuffix) TimeFormat.HOURS_SUFFIX else TimeFormat.HOURS).format