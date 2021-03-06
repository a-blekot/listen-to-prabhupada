package com.prabhupadalectures.common.lectures_impl.utils

import kotlin.native.concurrent.SharedImmutable

@SharedImmutable
const val FILE_EXTENSION = "mp3"

lateinit var DOWNLOADS_DIR: String

fun Boolean.toLong() = if (this) 1L else 0L

fun Boolean.toConnectionState() = if (this) ConnectionState.Online else ConnectionState.Offline

fun Long.toBoolean() = this > 0L

fun String.toValidUrl() = this
//    this
//        .replace("(", "%28")
//        .replace(")", "%29")
