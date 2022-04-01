package com.anadi.prabhupadalectures.android

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ProcessLifecycleOwner
import cafe.adriel.voyager.core.registry.ScreenRegistry
import com.anadi.prabhupadalectures.android.base.navigation.ScreenModules
import com.anadi.prabhupadalectures.data.Database
import com.anadi.prabhupadalectures.deepLink
import com.anadi.prabhupadalectures.repository.ResultsRepository
import com.anadi.prabhupadalectures.utils.DOWNLOADS_DIR
import com.anadi.prabhupadalectures.utils.ShareAction
import dagger.hilt.android.HiltAndroidApp
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import java.io.File
import javax.inject.Inject

@HiltAndroidApp
class PrabhupadaApp : Application() {

    companion object {
        lateinit var app: PrabhupadaApp
    }

    var shareAction: ShareAction? = null

    private val lifecycleEventObserver = LifecycleEventObserver { _, event ->
        when (event) {
            Lifecycle.Event.ON_CREATE -> DebugLog.d("_LIFECYCLE", "APPLICATION ON_CREATE")
            Lifecycle.Event.ON_START -> DebugLog.d("_LIFECYCLE", "APPLICATION ON_START")
            Lifecycle.Event.ON_RESUME -> DebugLog.d("_LIFECYCLE", "APPLICATION ON_RESUME")
            Lifecycle.Event.ON_PAUSE -> DebugLog.d("_LIFECYCLE", "APPLICATION ON_PAUSE")
            Lifecycle.Event.ON_STOP -> DebugLog.d("_LIFECYCLE", "APPLICATION ON_STOP")
            Lifecycle.Event.ON_DESTROY -> DebugLog.d("_LIFECYCLE", "APPLICATION ON_DESTROY")
            else -> {
                /** do nothing **/
                /** do nothing **/
            }
        }
    }

    private val activityLifecycleCallbacks = object : ActivityLifecycleCallbacks {
        override fun onActivityPaused(activity: Activity) {
            DebugLog.d("_LIFECYCLE", "onActivityPaused")
        }

        override fun onActivityStarted(activity: Activity) {
            DebugLog.d("_LIFECYCLE", "onActivityStarted")
        }

        override fun onActivityDestroyed(activity: Activity) {
            DebugLog.d("_LIFECYCLE", "onActivityDestroyed")
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
            DebugLog.d("_LIFECYCLE", "onActivitySaveInstanceState")
        }

        override fun onActivityStopped(activity: Activity) {
            DebugLog.d("_LIFECYCLE", "onActivityStopped")
        }

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            DebugLog.d("_LIFECYCLE", "onActivityCreated")
        }

        override fun onActivityResumed(activity: Activity) {
            DebugLog.d("_LIFECYCLE", "onActivityResumed")
            currentActivity = activity
        }
    }

    @Volatile
    var currentActivity: Activity? = null
        private set

    @Inject
    lateinit var db: Database

    @Inject
    lateinit var resultsRepository: ResultsRepository

    @Inject
    lateinit var backgroundScope: CoroutineScope

    @Inject
    lateinit var screenModules: ScreenModules

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Napier.base(DebugAntilog())
        }

        ScreenRegistry {
            screenModules.forEach { it() }
        }

        Napier.d("КРИШНА ХАРЕ РАМА")

        registerActivityLifecycleCallbacks(activityLifecycleCallbacks)
        ProcessLifecycleOwner.get().lifecycle.addObserver(lifecycleEventObserver)

        app = this
        DOWNLOADS_DIR = app.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)?.path ?: ""

        checkDownloadedFiles()
    }

    fun share(shareAction: ShareAction) {
        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/html"
            putExtra(Intent.EXTRA_TEXT, shareAction.deepLink)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        currentActivity?.startActivity(shareIntent)
    }

    private fun checkDownloadedFiles() {
        db.selectAllDownloads().forEach { lecture ->
            lecture.fileUrl.let { url ->
                if (url == null || !File(url).exists()) {
                    db.deleteFromDownloadsOnly(lecture)
                }
            }
        }
    }
}