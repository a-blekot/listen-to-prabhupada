package com.anadi.prabhupadalectures.android.player

import android.Manifest.permission.WAKE_LOCK
import android.app.Notification
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Binder
import android.os.IBinder
import android.os.PowerManager
import com.anadi.prabhupadalectures.android.util.cancelSafely
import com.anadi.prabhupadalectures.repository.PlaybackRepository
import com.anadi.prabhupadalectures.repository.ResultsRepository
import com.anadi.prabhupadalectures.repository.ToolsRepository
import dagger.hilt.android.AndroidEntryPoint
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
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
        Napier.d("onActivityStarted", tag = "PlaybackService")
        stopForeground(true)
        Napier.d("stopForeground", tag = "PlaybackService")
        player?.hideNotification()
    }

    fun onActivityStopped() =
        player?.run {
            Napier.d("onActivityStopped", tag = "PlaybackService")
            Napier.d("isPlaying = $isPlaying", tag = "PlaybackService")

            if (isPlaying) {
                showNotification()
            } else {
                stop()
            }
        }

    override fun onBind(intent: Intent): IBinder {
        Napier.d("onBind", tag = "PlaybackService")
        return binder
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Napier.d("onStartCommand", tag = "PlaybackService")
        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        Napier.d("onCreate", tag = "PlaybackService")
        player = Player(this, playbackRepository, tools, playerScope, this)

        requireWakeLock()
    }

    override fun onDestroy() {
        Napier.d("onDestroy", tag = "PlaybackService")
        releaseWakeLock()
        playerScope.cancelSafely("Service.onDestroy")
        super.onDestroy()
    }

    override fun onNotificationPosted(notification: Notification) {
        Napier.d("startForeground notification = $notification", tag = "PlaybackService")
        startForeground(NOTIFICATION_ID, notification)
    }

    override fun onNotificationCancelled() {
        Napier.d("onNotificationCancelled", tag = "PlaybackService")
        stop()
    }

    private fun stop() {
        Napier.d("stop", tag = "PlaybackService")
        stopForeground(true)
        Napier.d("stopForeground", tag = "PlaybackService")
        if (player?.isPlaying == false) {
            stopSelf()
            Napier.d("stopSelf", tag = "PlaybackService")
        }
    }

    private fun requireWakeLock(duration: Long = player?.totalDurationMillis ?: 0L) {
        if (packageManager.checkPermission(WAKE_LOCK, packageName) == PERMISSION_GRANTED) {
            try {
                val manager = getSystemService(POWER_SERVICE) as? PowerManager
                wakeLock = manager?.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, application.packageName)
                wakeLock?.acquire(duration)
                Napier.d("acquireWakeLock", tag = "PlaybackService")
            } catch (e: Throwable) {
                Napier.e("acquireWakeLock error", e, tag = "PlaybackService")
                /** do nothing */
            }
        }
    }

    private fun releaseWakeLock() =
        wakeLock?.apply {
            Napier.d("releaseWakeLock", tag = "PlaybackService")
            if (isHeld) release()
        }
}
