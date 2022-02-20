package com.anadi.prabhupadalectures.android

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import androidx.core.app.NotificationCompat
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerNotificationManager



class PlaybackService : Service() {


    private val binder = PlaybackBinder()
    private var wakeLock: PowerManager.WakeLock? = null

    private var pendingStartForegroundAction: (() -> Unit)? = null

    override fun onBind(intent: Intent): IBinder? {
        DebugLog.d("PlaybackService", "onBind")
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
//        intent.run {
//            val url = getStringExtra(EXTRA_LECTURE_URL)
//            currentTitle = getStringExtra(EXTRA_LECTURE_TITLE)
//            currentDescription = getStringExtra(EXTRA_LECTURE_DESCRIPTION)
//            currentDuration = getLongExtra(EXTRA_LECTURE_DURATION, 0L)
//            DebugLog.d("PlaybackService", "$currentTitle")
//
//            url?.let { playLecture(it) }
//        }
//
        return START_REDELIVER_INTENT
    }

    override fun onCreate() {
        super.onCreate()
        DebugLog.d("PlaybackService", "onCreate")
//        exoPlayer = ExoPlayer.Builder(this)
//            .build().apply { addListener(playbackListener) }
//        playerNotificationManager.setPlayer(exoPlayer)
    }

    override fun onDestroy() {
        DebugLog.d("PlaybackService", "onDestroy")
//        playerNotificationManager.setPlayer(null)
//        exoPlayer?.release()
//        exoPlayer = null
//        releaseWakeLock()
        super.onDestroy()
    }

//    override fun onTaskRemoved(rootIntent: Intent?) {
//        DebugLog.d("PlaybackService", "onTaskRemoved")
//        super.onTaskRemoved(rootIntent)
//        stopSelf()
//    }

    fun goForeground() {
        pendingStartForegroundAction?.invoke()
    }

    private fun playLecture(url: String) {
        DebugLog.d("PlaybackService", "playLecture")
//        exoPlayer?.apply {
//            addMediaItem(MediaItem.fromUri(url))
//            prepare()
//            playWhenReady = true
//            requireWakeLock()
//        }
    }

//    private fun requireWakeLock() =
//        currentDuration?.let {
//            try {
//                val manager = getSystemService(POWER_SERVICE) as? PowerManager
//                wakeLock = manager?.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, application.packageName)
//                wakeLock?.acquire(it)
//                DebugLog.d("PlaybackService", "requireWakeLock")
//            } catch (e: Throwable) {
//                DebugLog.e("PlaybackService", "requireWakeLock error", e)
//                /** do nothing */
//            }
//        }

    private fun releaseWakeLock() =
        wakeLock?.apply {
            DebugLog.d("PlaybackService", "releaseWakeLock")
            if (isHeld) release()
        }

    inner class PlaybackBinder : Binder() {
        val service: PlaybackService
            get() = this@PlaybackService
    }
}