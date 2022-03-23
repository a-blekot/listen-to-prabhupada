package com.anadi.prabhupadalectures.android.player

import android.Manifest.permission.WAKE_LOCK
import android.app.Notification
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Binder
import android.os.IBinder
import android.os.PowerManager
import com.anadi.prabhupadalectures.android.DebugLog
import com.anadi.prabhupadalectures.android.util.cancelSafely
import com.anadi.prabhupadalectures.repository.PlaybackRepository
import com.anadi.prabhupadalectures.repository.ResultsRepository
import com.anadi.prabhupadalectures.repository.ToolsRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.collect
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
    lateinit var resultsRepository: ResultsRepository

    @Inject
    lateinit var playbackRepository: PlaybackRepository

    @Inject
    lateinit var tools: ToolsRepository

    private val playerScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    fun onActivityStarted() {
        stopForeground(true)
        player?.hideNotification()
    }

    fun onActivityStopped() =
        player?.run {
            DebugLog.d("PlaybackService", "onActivityStopped")
            DebugLog.d("PlaybackService", "isPlaying = $isPlaying")

            if (isPlaying) {
                showNotification()
                DebugLog.d("PlaybackService", "showNotification")
            } else {
                stopForeground(true)
                stopSelf()
            }
        }

    override fun onBind(intent: Intent): IBinder {
        DebugLog.d("PlaybackService", "onBind")
        return binder
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        DebugLog.d("PlaybackService", "onStartCommand")
        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        DebugLog.d("PlaybackService", "onCreate")
        player = Player(this, playbackRepository, tools, playerScope, this)

        requireWakeLock()
    }

    override fun onDestroy() {
        DebugLog.d("PlaybackService", "onDestroy")
        releaseWakeLock()
        playerScope.cancelSafely("Service.onDestroy")
        super.onDestroy()
    }

    override fun onFinished() {
//        TODO("Not yet implemented")
    }

    override fun onNotificationPosted(notification: Notification) {
        DebugLog.d("PlaybackService", "startForeground notification = $notification")
        startForeground(NOTIFICATION_ID, notification)
    }

    override fun onNotificationCancelled() {
//        TODO("Not yet implemented")
        DebugLog.d("PlaybackService", "onNotificationCancelled")
        stopForeground(true)
//        stopSelf()
    }

    override fun onPlaybackStarted(timeLeft: Long) {
//        TODO("Not yet implemented")
    }

    private fun requireWakeLock(duration: Long = player?.totalDurationMillis ?: 0L) {
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
}
