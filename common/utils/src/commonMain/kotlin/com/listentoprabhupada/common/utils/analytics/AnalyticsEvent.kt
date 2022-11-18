package com.listentoprabhupada.common.utils.analytics

enum class AnalyticsEvent(val title: String) {
    PRERATING_SHOWN("prerating_shown"),
    PRERATING_ACCEPTED("prerating_accepted"),
    PRERATING_CLOSED("prerating_closed"),
    TUTORIAL_OPEN("tutorial_open"),
    TUTORIAL_SETTINGS("tutorial_settings"),
    TUTORIAL_SKIP("tutorial_skip"),
    TUTORIAL_COMPLETE("tutorial_complete"),
    PLAY_LECTURE("play_lecture"),
    PLAY_COMPLETED("play_completed")
}
