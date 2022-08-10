@file:JvmName("DateUtils")

package com.listentoprabhupada.android_ui.utils

import java.util.*
import java.util.concurrent.TimeUnit.*
import java.util.concurrent.atomic.AtomicLong

val minutesRange = Long.MIN_VALUE until HOURS.toMillis(1)
val hoursRange = HOURS.toMillis(1) until DAYS.toMillis(1)

val ONE_MINUTE_MS = MINUTES.toMillis(1)
val TWO_MINUTES_MS = MINUTES.toMillis(2)
val ONE_HOUR_MS = HOURS.toMillis(1)
val TWO_HOURS_MS = HOURS.toMillis(2)
val ONE_DAY_MS = DAYS.toMillis(1)
val TWO_DAYS_MS = DAYS.toMillis(2)

val serverTimeDiff = AtomicLong(0L)

val now: Long
    get() = System.currentTimeMillis() + serverTimeDiff.get()

val systemTime: Long
    get() = System.currentTimeMillis()

fun formattedDateString(format: String, vararg args: Any?) =
    String.format(Locale.US, format, *args)

fun formatExpiredTimeWithAbbreviation(timeMillis: Long) =
    when (timeMillis) {
        in minutesRange -> minutes(timeMillis).coerceAtLeast(1).run { "$this min" }
        in hoursRange -> hours(timeMillis).run { if (this <= 1L) "1 hour" else "$this hours" }
        else -> days(timeMillis).run { if (this <= 1L) "1 day" else "$this days" }
    }

fun formatTimeForDialogs(timeMillis: Long) =
    formatTimeHoursMinutesSeconds(timeMillis, TimeFormat.DIALOGS_TIMER.format)

fun formatTimeAdaptiveDaysMax(timeMillis: Long, showDateShortSuffix: Boolean = true) =
    if (timeMillis >= DAYS.toMillis(1)) {
        formatTimeDaysHours(timeMillis)
    } else {
        formatTimeAdaptiveHoursMax(timeMillis, showDateShortSuffix)
    }

fun formatTimeAdaptiveHoursMax(timeMillis: Long, showDateShortSuffix: Boolean = true) =
    if (timeMillis >= HOURS.toMillis(1)) {
        formatTimeHoursMinutes(timeMillis, showDateShortSuffix)
    } else {
        formatTimeMinutesSeconds(timeMillis, showDateShortSuffix)
    }

fun formatTimeDaysHours(timeMillis: Long) =
    getDaysHours(timeMillis).run { formattedDateString(TimeFormat.DAYS.format, days, hours) }

fun formatTimeHoursMinutes(timeMillis: Long, showSuffix: Boolean) =
    getHoursMinutes(timeMillis).run { formattedDateString(getTimeFormatHours(showSuffix), hours, minutes) }

fun formatTimeMinutesSeconds(timeMillis: Long, showSuffix: Boolean) =
    getMinutesSeconds(timeMillis).run { formattedDateString(getTimeFormatMinutes(showSuffix), minutes, seconds) }

fun formatTimeHoursMinutesSeconds(timeMillis: Long, customFormatFor3DigitIndicator: String? = null) =
    getHoursMinutesSeconds(timeMillis).run {
        formattedDateString(
            customFormatFor3DigitIndicator ?: TimeFormat.HOURS_MINUTES_SECONDS.format,
            hours,
            minutes,
            seconds
        )
    }

fun seconds(timeMillis: Long) =
    MILLISECONDS.toSeconds(timeMillis.atLeast(0))

fun minutes(timeMillis: Long) =
    MILLISECONDS.toMinutes(timeMillis.atLeast(0))

fun hours(timeMillis: Long) =
    MILLISECONDS.toHours(timeMillis.atLeast(0))

fun days(timeMillis: Long) =
    MILLISECONDS.toDays(timeMillis.atLeast(0))

fun Int.twoDigitsFormat() =
    String.format("%1$02d", this)

private fun getMinutesSeconds(timeMillis: Long): Time {
    val minutes = minutes(timeMillis)
    return Time(minutes = minutes, seconds = seconds(timeMillis) - MINUTES.toSeconds(minutes))
}

private fun getHoursMinutes(timeMillis: Long): Time {
    val hours = hours(timeMillis)
    return Time(hours = hours, minutes = minutes(timeMillis) - HOURS.toMinutes(hours))
}

private fun getHoursMinutesSeconds(timeMillis: Long): Time {
    val hours = hours(timeMillis)
    val minutes = minutes(timeMillis)
    return Time(
        hours = hours,
        minutes = minutes - HOURS.toMinutes(hours),
        seconds = seconds(timeMillis) - MINUTES.toSeconds(minutes)
    )
}

private fun getDaysHours(timeMillis: Long): Time {
    val days = days(timeMillis)
    return Time(days = days, hours = hours(timeMillis) - DAYS.toHours(days))
}

private fun Long.atLeast(minValue: Long) =
    coerceAtLeast(minValue)

private class Time(
    val days: Long = 0,
    val hours: Long = 0,
    val minutes: Long = 0,
    val seconds: Long = 0,
)
