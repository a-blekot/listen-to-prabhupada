package com.listentoprabhupada.android.inapp

import android.app.Activity
import com.google.android.play.core.review.ReviewManagerFactory
import com.listentoprabhupada.android.PrabhupadaApp.Companion.app
import com.listentoprabhupada.common.settings.onInappReviewShown
import io.github.aakira.napier.Napier

fun showInappReview(activity: Activity, onNext: () -> Unit = {}) {
    val reviewManager = ReviewManagerFactory.create(app)
    val requestReviewFlow = reviewManager.requestReviewFlow()
    requestReviewFlow.addOnCompleteListener { request ->
        if (request.isSuccessful) {
            val reviewInfo = request.result
            reviewManager.launchReviewFlow(activity, reviewInfo).apply {
                addOnCompleteListener {
                    onInappReviewShown()
                    onNext()
                }
            }
        } else {
            request.exception?.let { Napier.e("InappReview", it, "requestReviewFlow is not successful") }
            onNext()
        }
    }
}
