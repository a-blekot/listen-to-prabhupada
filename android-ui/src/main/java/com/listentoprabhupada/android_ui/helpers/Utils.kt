package com.listentoprabhupada.android_ui.helpers

fun <T> selector(positive: T, negative: T, condition: Boolean): T =
    if (condition) positive else negative