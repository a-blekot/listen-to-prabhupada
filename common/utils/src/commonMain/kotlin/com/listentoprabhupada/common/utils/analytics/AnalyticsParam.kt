package com.listentoprabhupada.common.utils.analytics

enum class AnalyticsParam {
    COUNT,
    SCREEN,
    LAUNCH_COUNT,
    DURATION,
    LECTURE_ID;

    val low
        get() = name.lowercase()
}