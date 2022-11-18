package com.listentoprabhupada.common.utils.analytics

import com.listentoprabhupada.common.utils.analytics.AnalyticsEvent.TUTORIAL_OPEN
import com.listentoprabhupada.common.utils.analytics.AnalyticsParam.*
import com.listentoprabhupada.common.settings.getAppLaunchCount
import com.listentoprabhupada.common.settings.getPreRatingClosedCount
import com.listentoprabhupada.common.settings.getPreRatingShownCount
import com.listentoprabhupada.common.settings.getTutorialSkippCount

interface Analytics {
    fun logEvent(event: AnalyticsEvent, params: Map<String, Any>? = null)
}

fun Analytics.playLecture(lectureId: String) =
    logEvent(
        AnalyticsEvent.PLAY_LECTURE,
        mapOf(
            LECTURE_ID.low to lectureId,
        )
    )

fun Analytics.playCompleted(lectureId: String, durationSec: Long) =
    logEvent(
        AnalyticsEvent.PLAY_COMPLETED,
        mapOf(
            LECTURE_ID.low to lectureId,
            DURATION.low to durationSec
        )
    )

fun Analytics.preratingShown() =
    logEvent(
        AnalyticsEvent.PRERATING_SHOWN,
        mapOf(COUNT.low to getPreRatingShownCount())
    )

fun Analytics.preratingAccepted() =
    logEvent(
        AnalyticsEvent.PRERATING_ACCEPTED,
    )

fun Analytics.preratingClosed() =
    logEvent(
        AnalyticsEvent.PRERATING_CLOSED,
        mapOf(COUNT.low to getPreRatingClosedCount())
    )

fun Analytics.tutorialOpen() =
    logEvent(
        TUTORIAL_OPEN,
        mapOf(LAUNCH_COUNT.low to getAppLaunchCount())
    )

fun Analytics.tutorialSettings() =
    logEvent(AnalyticsEvent.TUTORIAL_SETTINGS)

fun Analytics.tutorialSkip() =
    logEvent(
        AnalyticsEvent.TUTORIAL_SKIP,
        mapOf(COUNT.low to getTutorialSkippCount())
    )

fun Analytics.tutorialComplete(screen: AnalyticsScreen) =
    logEvent(
        AnalyticsEvent.TUTORIAL_COMPLETE,
        mapOf(SCREEN.low to screen.name)
    )
