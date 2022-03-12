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
import cafe.adriel.voyager.navigator.Navigator
import com.anadi.prabhupadalectures.android.player.PlaybackService
import com.anadi.prabhupadalectures.android.ui.compose.*
import com.anadi.prabhupadalectures.repository.Repository
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity(), ServiceConnection {

    private val uiListener: (UIAction) -> Unit = { uiAction ->
        when (uiAction) {
            is Option -> {
                lifecycleScope.launchWhenStarted {
                    repository.updateQuery(uiAction.queryParam)
                }
            }
            is Favorite -> {
                if (uiAction.isFavorite) {
                    repository.addFavorite(uiAction.lectureId)
                } else {
                    repository.removeFavorite(uiAction.lectureId)
                }
            }
            else -> playbackService?.handleAction(uiAction)
        }
    }

    private val serviceIntent
        get() = Intent(this, PlaybackService::class.java)

    private var playbackService: PlaybackService? = null

    @Inject
    lateinit var repository: Repository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme {
                ProvideWindowInsets {
                    Box(
                        Modifier.padding(
                            rememberInsetsPaddingValues(
                                insets = LocalWindowInsets.current.systemBars,
                                applyStart = true,
                                applyTop = false,
                                applyEnd = true,
                                applyBottom = false
                            )
                        )
                    ) {
                        Navigator(MainScreen(repository, uiListener))
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        DebugLog.d("PlaybackService", "ACTIVITY startService")
        startService(serviceIntent)
        lifecycleScope.launchWhenStarted {
            delay(300L)
            // TODO applicationContext.bindService ??
            bindService(serviceIntent, this@MainActivity, Context.BIND_IMPORTANT)
        }
    }

    override fun onStop() {
        playbackService?.onActivityStopped()
        unbindService(this)
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
