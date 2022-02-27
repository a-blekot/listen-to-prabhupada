package com.anadi.prabhupadalectures.android.player

import android.Manifest.permission.WAKE_LOCK
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Binder
import android.os.IBinder
import android.os.PowerManager
import com.anadi.prabhupadalectures.android.DebugLog
import com.anadi.prabhupadalectures.android.ui.compose.UIAction
import com.anadi.prabhupadalectures.repository.Repository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import java.lang.IllegalStateException
import javax.inject.Inject

@AndroidEntryPoint
class PlaybackService : Service(), Player.Listener {

    inner class PlaybackBinder : Binder() {
        val service: PlaybackService
            get() = this@PlaybackService
    }

    private val binder = PlaybackBinder()
    private var player: Player? = null
    private var wakeLock: PowerManager.WakeLock? = null

    @Inject
    lateinit var repository: Repository

    private val playerScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    fun onActivityStarted() {
        stopForeground(true)
        player?.hideNotification()
    }

    fun onActivityStopped() =
        player?.run {
            notification?.let { startForeground(NOTIFICATION_ID, it) }
            showNotification()
        }

    override fun onBind(intent: Intent): IBinder {
        DebugLog.d("PlaybackService", "onBind")
        return binder
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        return START_REDELIVER_INTENT
    }

    override fun onCreate() {
        super.onCreate()
        DebugLog.d("PlaybackService", "onCreate")
        player = Player(this, repository, playerScope,this)

        playerScope.launch {
            repository.observeState().collect {
                player?.setPlaylist(it.playlist)
            }
        }
    }

    override fun onDestroy() {
        DebugLog.d("PlaybackService", "onDestroy")
        releaseWakeLock()
        playerScope.cancelSafely("Service.onDestroy")
        super.onDestroy()
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        DebugLog.d("PlaybackService", "onTaskRemoved")
        super.onTaskRemoved(rootIntent)
        stopSelf()
    }

    override fun onFinished() {
        TODO("Not yet implemented")
    }

    override fun onNotificationCancelled() {
        TODO("Not yet implemented")
    }

    override fun onPlaybackStarted(timeLeft: Long) {
        TODO("Not yet implemented")
    }

    fun handleAction(uiAction: UIAction) =
        player?.handleAction(uiAction)

    private fun requireWakeLock(duration: Long) {
        if (packageManager.checkPermission(WAKE_LOCK, packageName) == PERMISSION_GRANTED) {
            try {
                val manager = getSystemService(POWER_SERVICE) as? PowerManager
                wakeLock = manager?.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, application.packageName)
                wakeLock?.acquire(duration)
                DebugLog.d("PlaybackService", "requireWakeLock")
            } catch (e: Throwable) {
                DebugLog.e("PlaybackService", "requireWakeLock error", e)
                /** do nothing */
            }
        }
    }

    private fun releaseWakeLock() =
        wakeLock?.apply {
            DebugLog.d("PlaybackService", "releaseWakeLock")
            if (isHeld) release()
        }

    private fun CoroutineScope.cancelSafely(msg: String) {
        try {
            if (isActive) {
                cancel(msg)
            }
        } catch (e: IllegalStateException) {

        }
    }
}
