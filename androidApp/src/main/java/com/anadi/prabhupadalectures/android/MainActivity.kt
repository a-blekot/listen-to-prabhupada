package com.anadi.prabhupadalectures.android

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.anadi.prabhupadalectures.android.PrabhupadaApp.Companion.app
import com.anadi.prabhupadalectures.android.base.navigation.NavigatorWrapper
import com.anadi.prabhupadalectures.android.download.DownloadService
import com.anadi.prabhupadalectures.android.download.DownloadServiceAction
import com.anadi.prabhupadalectures.android.player.PlaybackService
import com.anadi.prabhupadalectures.android.ui.compose.AppTheme
import com.anadi.prabhupadalectures.android.util.parseShareAction
import com.anadi.prabhupadalectures.repository.ResultsRepository
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity(), ServiceConnection {

    private val playbackServiceIntent
        get() = Intent(this, PlaybackService::class.java)

    private val downloadServiceIntent
        get() = Intent(this, DownloadService::class.java)

    private var playbackService: PlaybackService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        app.shareAction = parseShareAction(intent.data)
        intent.data = null

        setContent {
            AppTheme {
                ProvideWindowInsets {
                    Box(
                        Modifier.padding(
                            rememberInsetsPaddingValues(
                                insets = LocalWindowInsets.current.systemBars,
                                applyStart = true,
                                applyTop = true,
                                applyEnd = true,
                                applyBottom = true
                            )
                        )
                    ) {
                        NavigatorWrapper()
                    }
                }
            }
        }
    }

    @Inject
    lateinit var resultsRepository: ResultsRepository

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        lifecycleScope.launchWhenCreated {
            parseShareAction(intent?.data).let {
                resultsRepository.init(it)
            }
        }
        setIntent(null)
    }

    override fun onStart() {
        super.onStart()
        DebugLog.d("PlaybackService", "ACTIVITY startService")
        startService(playbackServiceIntent)
        lifecycleScope.launchWhenStarted {
            delay(500L)
            applicationContext.bindService(playbackServiceIntent, this@MainActivity, Context.BIND_IMPORTANT)
        }

        downloadServiceIntent
            .apply { action = DownloadServiceAction.ON_ACTIVITY_START.name }
            .let { intent -> startService(intent) }
    }

    override fun onStop() {
        downloadServiceIntent
            .apply { action = DownloadServiceAction.ON_ACTIVITY_STOP.name }
            .let { intent -> startService(intent) }

        playbackService?.onActivityStopped()
        applicationContext.unbindService(this)
        super.onStop()
    }

    override fun onDestroy() {
        DebugLog.d("PlaybackService", "ACTIVITY onDestroy")
        super.onDestroy()
    }

    override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
        playbackService = (binder as? PlaybackService.PlaybackBinder)?.service
        playbackService?.onActivityStarted()
        DebugLog.d("PlaybackService", "ACTIVITY onServiceConnected")
        DebugLog.d("PlaybackService", "ACTIVITY boundService = $playbackService")
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        playbackService = null
        DebugLog.d("PlaybackService", "ACTIVITY onServiceDisconnected")
        DebugLog.d("PlaybackService", "ACTIVITY boundService = $playbackService")
    }
}
