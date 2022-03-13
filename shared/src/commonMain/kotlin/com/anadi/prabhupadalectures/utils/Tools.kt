package com.anadi.prabhupadalectures.utils

fun Boolean.toLong() = if(this) 1L else 0L

fun String.toLong() = hashCode().toLong()

fun Long.toBoolean() = this > 0L