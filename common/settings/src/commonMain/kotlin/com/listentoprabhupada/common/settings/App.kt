package com.listentoprabhupada.common.settings


private const val APP_LAUNCH_COUNT = "APP_LAUNCH_COUNT"
private const val INAPP_REVIEW_SHOWN = "INAPP_REVIEW_SHOWN"
private const val LOCALE_KEY = "LOCALE_KEY"
private const val PRERATING_CLOSED_COUNT = "PRERATING_CLOSED_COUNT"
private const val PRERATING_SHOWN_COUNT = "PRERATING_SHOWN_COUNT"
private const val TUTORIAL_COMPLETED_KEY = "TUTORIAL_COMPLETED_KEY"
private const val TUTORIAL_SKIPP_COUNT_KEY = "TUTORIAL_SKIPP_COUNT_KEY"

var tutorialWasShownInThisSession = false

fun onAppLaunch() =
    getAppLaunchCount().let {
        settings.putInt(APP_LAUNCH_COUNT, it + 1)
    }

fun getAppLaunchCount() =
    settings.getInt(APP_LAUNCH_COUNT)

fun inappReviewShown() =
    settings.getBoolean(INAPP_REVIEW_SHOWN)

fun onInappReviewShown() =
    settings.putBoolean(INAPP_REVIEW_SHOWN, true)

fun saveLocale(locale: String) =
    settings.putString(LOCALE_KEY, locale)

fun getLocale() =
    settings.getString(LOCALE_KEY)

fun isTutorialCompleted() =
    settings.getBoolean(TUTORIAL_COMPLETED_KEY)

fun setTutorialCompleted() =
    settings.putBoolean(TUTORIAL_COMPLETED_KEY, true)

fun onTutorialSkipped() =
    getTutorialSkippCount().let {
        settings.putInt(TUTORIAL_SKIPP_COUNT_KEY, it + 1)
    }

fun getTutorialSkippCount() =
    settings.getInt(TUTORIAL_SKIPP_COUNT_KEY)

fun onPreRatingShown() =
    getPreRatingShownCount().let {
        settings.putInt(PRERATING_SHOWN_COUNT, it + 1)
    }

fun getPreRatingShownCount() =
    settings.getInt(PRERATING_SHOWN_COUNT)

fun onPreRatingClosed() =
    getPreRatingClosedCount().let {
        settings.putInt(PRERATING_CLOSED_COUNT, it + 1)
    }

fun getPreRatingClosedCount() =
    settings.getInt(PRERATING_CLOSED_COUNT)
