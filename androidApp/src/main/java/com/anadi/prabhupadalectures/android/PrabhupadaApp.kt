package com.anadi.prabhupadalectures.android

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.anadi.prabhupadalectures.data.DatabaseImpl
import com.anadi.prabhupadalectures.data.DatabaseDriverFactory
import com.anadi.prabhupadalectures.repository.RepositoryImpl
import com.anadi.prabhupadalectures.network.api.createPrabhupadaApi
import com.anadi.prabhupadalectures.repository.Repository
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltAndroidApp
class PrabhupadaApp : Application() {

    companion object {
        lateinit var app: PrabhupadaApp
    }

    private val activityLifecycleCallbacks = object : ActivityLifecycleCallbacks {
        override fun onActivityPaused(activity: Activity) {
            DebugLog.d("ACTIVITY_LIFECYCLE", "onActivityPaused")
        }
        override fun onActivityStarted(activity: Activity) {
            DebugLog.d("ACTIVITY_LIFECYCLE", "onActivityStarted")
        }
        override fun onActivityDestroyed(activity: Activity) {
            DebugLog.d("ACTIVITY_LIFECYCLE", "onActivityDestroyed")
        }
        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
            DebugLog.d("ACTIVITY_LIFECYCLE", "onActivitySaveInstanceState")
        }
        override fun onActivityStopped(activity: Activity) {
            DebugLog.d("ACTIVITY_LIFECYCLE", "onActivityStopped")
        }
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            DebugLog.d("ACTIVITY_LIFECYCLE", "onActivityCreated")
        }
        override fun onActivityResumed(activity: Activity) {
            DebugLog.d("ACTIVITY_LIFECYCLE", "onActivityResumed")
        }
    }

    @Inject
    lateinit var repository: Repository

    @Inject
    lateinit var backgroundScope: CoroutineScope

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(activityLifecycleCallbacks)

        app = this

        backgroundScope.launch {
            delay(200L)
            repository.init()
        }
    }
}