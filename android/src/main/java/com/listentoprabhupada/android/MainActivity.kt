package com.listentoprabhupada.android

import com.listentoprabhupada.common.utils.resources.AndroidStringResourceHandler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.listentoprabhupada.android.inapp.BillingHelperAndroid
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.defaultComponentContext
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.listentoprabhupada.android.PrabhupadaApp.Companion.app
import com.listentoprabhupada.android.download.DownloadService
import com.listentoprabhupada.android.download.DownloadServiceAction
import com.listentoprabhupada.android.inapp.InappUpdater
import com.listentoprabhupada.android.inapp.showInappReview
import com.listentoprabhupada.android.player.PlaybackService
import com.listentoprabhupada.android.util.parseShareAction
import com.listentoprabhupada.android_ui.screens.root.RootContent
import com.listentoprabhupada.android_ui.theme.AppTheme
import com.listentoprabhupada.common.data.VersionInfo
import com.listentoprabhupada.common.root.RootComponent
import com.listentoprabhupada.common.root.RootComponentImpl
import com.listentoprabhupada.common.root.RootDeps
import com.listentoprabhupada.common.utils.LogTag
import com.listentoprabhupada.common.utils.RemoteConfig
import com.listentoprabhupada.common.utils.billing.BillingHelper
import com.listentoprabhupada.common.utils.dispatchers.dispatchers
import io.github.aakira.napier.Napier
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            playbackService = (binder as? PlaybackService.PlaybackBinder)?.service
            playbackService?.setPlayerBus(app.playerBus)
            playbackService?.onActivityStarted()
            Napier.d("ACTIVITY onServiceConnected", tag = LogTag.playbackService)
            Napier.d("ACTIVITY boundService = $playbackService", tag = LogTag.playbackService)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            playbackService = null
            Napier.d("ACTIVITY onServiceDisconnected", tag = LogTag.playbackService)
            Napier.d("ACTIVITY boundService = $playbackService", tag = LogTag.playbackService)
        }
    }

    private val playbackServiceIntent
        get() = Intent(this, PlaybackService::class.java)

    private val downloadServiceIntent
        get() = Intent(this, DownloadService::class.java)

    private var isBound = false
    private var playbackService: PlaybackService? = null
    private var billingHelper: BillingHelper? = null
    private var inappUpdater: InappUpdater? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        billingHelper = BillingHelperAndroid(this, this.lifecycleScope, app.connectivityObserver)
        inappUpdater = InappUpdater(this)

        app.shareAction = parseShareAction(intent.data)
        intent.data = null

        val root = root(defaultComponentContext())

        setContent {
            AppTheme {
                RootContent(root)
            }
        }
        bindService()
        app.connectivityObserver.start()
    }

    override fun onDestroy() {
        billingHelper?.clean()
        inappUpdater?.clean()
        app.connectivityObserver.stop()
        super.onDestroy()
    }

    override fun onStart() {
        super.onStart()
        Napier.d("ACTIVITY startService", tag = LogTag.playbackService)
        startService(playbackServiceIntent)

        downloadServiceIntent
            .apply { action = DownloadServiceAction.ON_ACTIVITY_START.name }
            .let { intent -> startService(intent) }
    }

    override fun onResume() {
        super.onResume()
        billingHelper?.checkUnconsumedPurchases()
        inappUpdater?.checkUpdate()
    }

    override fun onStop() {
        downloadServiceIntent
            .apply { action = DownloadServiceAction.ON_ACTIVITY_STOP.name }
            .let { intent -> startService(intent) }

        playbackService?.onActivityStopped()
        unbindService()
        super.onStop()
    }

    private fun root(componentContext: ComponentContext): RootComponent =
        RootComponentImpl(
            componentContext = componentContext,
            storeFactory = LoggingStoreFactory(DefaultStoreFactory()),
            deps = RootDeps(
                db = app.db,
                api = app.api,
                playerBus = app.playerBus,
                remoteConfig = RemoteConfig(),
                dispatchers = dispatchers(),
                connectivityObserver = app.connectivityObserver,
                stringResourceHandler = AndroidStringResourceHandler(this),
                billingHelper = billingHelper,
                analytics = app.analytics,
                versionInfo = VersionInfo(BuildConfig.VERSION_CODE, BuildConfig.VERSION_NAME),
                onEmail = ::sendEmail,
                onRateUs = ::rateUs,
                onShareApp = ::shareApp,
                onInappReview = ::inappReview,
            )
        )

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        lifecycleScope.launchWhenCreated {
            parseShareAction(intent?.data).let {
//                resultsRepository.init(it)
            }
        }
        setIntent(null)
    }

    private fun bindService() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                delay(500L)
                Napier.d("app.playerBus = ${app.playerBus}", tag="PlayerBus")
                isBound = applicationContext.bindService(playbackServiceIntent, serviceConnection, Context.BIND_IMPORTANT)
            }
        }
    }

    private fun unbindService() {
        if (isBound) {
            applicationContext.unbindService(serviceConnection)
            isBound = false
        }
    }

    private fun sendEmail() {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            val title = resources.getString(R.string.email_title)
            val body = resources.getString(R.string.email_body)

            val uriText =
                "mailto:listenprabhupada108@gmail.com?subject=$title&body=$body \uD83D\uDE07"
            data = Uri.parse(uriText)
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            Napier.e("No activity for $intent")
        }
    }

    private fun rateUs() {
        val uri: Uri = Uri.parse("market://details?id=$packageName")
        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or
            Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
            Intent.FLAG_ACTIVITY_MULTIPLE_TASK
        )

        if (goToMarket.resolveActivity(packageManager) != null) {
            startActivity(goToMarket)
        } else {
            Napier.e("No activity for $goToMarket")
        }
    }

    private fun shareApp() {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, "http://play.google.com/store/apps/details?id=$packageName")
        }
        Intent.createChooser(intent, "Share via")
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            Napier.e("No activity for $intent")
        }
    }

    private fun inappReview() =
        showInappReview(this)

    override fun startService(intent: Intent): ComponentName? =
        try {
            super.startService(intent)
        } catch (e: IllegalArgumentException) {
            // The process is classed as idle by the platform.
            // Starting a background service is not allowed in this state.
            Napier.d("Failed to start service (process is idle).", tag = "PLAYBACK_SERVICE")
            componentName
        } catch (e: IllegalStateException) {
            // The app is in background, starting service is disallow
            Napier.d("Failed to start service (app is in background)", tag = "PLAYBACK_SERVICE")
            componentName
        }
}
