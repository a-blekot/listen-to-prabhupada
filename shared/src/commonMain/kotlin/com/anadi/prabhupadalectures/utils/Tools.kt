package com.anadi.prabhupadalectures.utils

const val FILE_EXTENSION = "mp3"

lateinit var DOWNLOADS_DIR: String

fun Boolean.toLong() = if (this) 1L else 0L

fun Boolean.toConnectionState() = if (this) ConnectionState.Online else ConnectionState.Offline

fun String.toLong() = hashCode().toLong()

fun Long.toBoolean() = this > 0L