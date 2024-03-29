package com.listentoprabhupada.android

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.listentoprabhupada.android.util.AnalyticsAndroid
import com.listentoprabhupada.android.util.AnalyticsAndroidDebug
import com.listentoprabhupada.android.util.CrashlyticsAntilog
import com.listentoprabhupada.common.database.Database
import com.listentoprabhupada.common.database.DatabaseDriverFactory
import com.listentoprabhupada.common.database.DatabaseImpl
import com.listentoprabhupada.common.network.createPrabhupadaApi
import com.listentoprabhupada.common.results_impl.repository.*
import com.listentoprabhupada.common.player_api.PlayerBus
import com.listentoprabhupada.common.player_impl.PlayerBusImpl
import com.listentoprabhupada.common.settings.onAppLaunch
import com.listentoprabhupada.common.utils.*
import com.listentoprabhupada.common.utils.analytics.Analytics
import com.listentoprabhupada.common.utils.connectivity.ConnectivityObserver
import com.listentoprabhupada.common.utils.connectivity.ConnectivityObserverAndroid
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.io.File
import com.listentoprabhupada.common.utils.dispatchers.dispatchers
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

class PrabhupadaApp : Application() {

    companion object {
        lateinit var app: PrabhupadaApp
    }

    var shareAction: ShareAction? = null

    private val lifecycleEventObserver = LifecycleEventObserver { _, event ->
        when (event) {
            Lifecycle.Event.ON_CREATE -> Napier.d( "APPLICATION ON_CREATE", tag = LogTag.lifecycleApp)
            Lifecycle.Event.ON_START -> Napier.d( "APPLICATION ON_START", tag = LogTag.lifecycleApp)
            Lifecycle.Event.ON_RESUME -> Napier.d( "APPLICATION ON_RESUME", tag = LogTag.lifecycleApp)
            Lifecycle.Event.ON_PAUSE -> Napier.d( "APPLICATION ON_PAUSE", tag = LogTag.lifecycleApp)
            Lifecycle.Event.ON_STOP -> Napier.d( "APPLICATION ON_STOP", tag = LogTag.lifecycleApp)
            Lifecycle.Event.ON_DESTROY -> Napier.d( "APPLICATION ON_DESTROY", tag = LogTag.lifecycleApp)
            else -> {
                /** do nothing **/
            }
        }
    }

    private val activityLifecycleCallbacks = object : ActivityLifecycleCallbacks {
        override fun onActivityPaused(activity: Activity) {
            Napier.d( "onActivityPaused", tag = LogTag.lifecycleActivity)
        }

        override fun onActivityStarted(activity: Activity) {
            Napier.d( "onActivityStarted", tag = LogTag.lifecycleActivity)
        }

        override fun onActivityDestroyed(activity: Activity) {
            Napier.d( "onActivityDestroyed", tag = LogTag.lifecycleActivity)
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
            Napier.d( "onActivitySaveInstanceState", tag = LogTag.lifecycleActivity)
        }

        override fun onActivityStopped(activity: Activity) {
            Napier.d( "onActivityStopped", tag = LogTag.lifecycleActivity)
        }

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            Napier.d( "onActivityCreated", tag = LogTag.lifecycleActivity)
        }

        override fun onActivityResumed(activity: Activity) {
            Napier.d( "onActivityResumed", tag = LogTag.lifecycleActivity)
            currentActivity = activity
        }
    }

    @Volatile
    var currentActivity: Activity? = null
        private set

    private val bgScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    val api = createPrabhupadaApi()
    lateinit var db: Database
    lateinit var playerBus: PlayerBus
    lateinit var toolsRepository: ToolsRepository
    lateinit var downloadsRepository: DownloadsRepository
    lateinit var analytics: Analytics
    lateinit var connectivityObserver: ConnectivityObserver

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            analytics = AnalyticsAndroidDebug()
            FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(false)
            initNapier(DebugAntilog())
        } else {
            analytics = AnalyticsAndroid(this)
            FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
            initNapier(CrashlyticsAntilog(this))
        }

        registerActivityLifecycleCallbacks(activityLifecycleCallbacks)
        ProcessLifecycleOwner.get().lifecycle.addObserver(lifecycleEventObserver)

        app = this
        DOWNLOADS_DIR = app.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)?.path ?: ""

        connectivityObserver = ConnectivityObserverAndroid(this)
        db = DatabaseImpl(DatabaseDriverFactory(this))
        playerBus = PlayerBusImpl(dispatchers())
        Napier.d( "PlayerBusImpl created!!!!!!!!!!!!!!!!", tag = LogTag.lifecycleActivity)
        toolsRepository = ToolsRepositoryImpl(db)
        downloadsRepository = DownloadsRepositoryImpl(db, api)
        onAppLaunch()

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

    private fun checkDownloadedFiles() =
        bgScope.launch(Dispatchers.IO) {
            db.selectAllDownloads().forEach { lecture ->
                lecture.fileUrl.let { url ->
                    if (url == null || !File(url).exists()) {
                        db.deleteFromDownloadsOnly(lecture)
                    }
                }
            }
        }
}