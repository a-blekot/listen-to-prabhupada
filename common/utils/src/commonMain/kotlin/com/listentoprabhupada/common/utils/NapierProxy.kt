package com.listentoprabhupada.common.utils

import io.github.aakira.napier.Antilog
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

fun initNapier(antilog: Antilog) {
    Napier.base(antilog)
}

fun d(message: String, tag: String? = null) =
    Napier.d(message, tag = tag)

fun e(message: String, throwable: Throwable? = null, tag: String? = null) =
    Napier.e(message, throwable = throwable, tag = tag)