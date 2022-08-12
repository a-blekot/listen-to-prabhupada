package com.listentoprabhupada.android

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.defaultComponentContext
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.listentoprabhupada.android.PrabhupadaApp.Companion.app
import com.listentoprabhupada.android.download.DownloadService
import com.listentoprabhupada.android.download.DownloadServiceAction
import com.listentoprabhupada.android.player.PlaybackService
import com.listentoprabhupada.android.util.parseShareAction
import com.listentoprabhupada.android_ui.screens.root.RootContent
import com.listentoprabhupada.android_ui.theme.AppTheme
import com.listentoprabhupada.common.root.RootComponent
import com.listentoprabhupada.common.root.RootComponentImpl
import com.listentoprabhupada.common.root.RootDeps
import com.listentoprabhupada.common.utils.LogTag
import com.listentoprabhupada.common.utils.RemoteConfig
import com.listentoprabhupada.common.utils.dispatchers.dispatchers
import io.github.aakira.napier.Napier
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            playbackService = (binder as? PlaybackService.PlaybackBinder)?.service
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        app.shareAction = parseShareAction(intent.data)
        intent.data = null

        val root = root(defaultComponentContext())

        setContent {
            AppTheme {
                RootContent(
                    root,
                    Modifier.windowInsetsPadding(
                        WindowInsets.systemBars.only(WindowInsetsSides.Top)
                    )
                )
            }
        }
        bindService()
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
                dispatchers = dispatchers()
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

    override fun onStart() {
        super.onStart()
        Napier.d("ACTIVITY startService", tag = LogTag.playbackService)
        startService(playbackServiceIntent)
        downloadServiceIntent
            .apply { action = DownloadServiceAction.ON_ACTIVITY_START.name }
            .let { intent -> startService(intent) }
    }

    override fun onStop() {
        downloadServiceIntent
            .apply { action = DownloadServiceAction.ON_ACTIVITY_STOP.name }
            .let { intent -> startService(intent) }

        playbackService?.onActivityStopped()
        unbindService()
        super.onStop()
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
}
