package com.anadi.prabhupadalectures.android

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import android.util.Log
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import java.util.*

private const val NOTIFICATION_ID = 16108
private const val CHANNEL_ID = "PlaybackService_CHANNEL_16108"

class PlaybackService : Service() {

    private val binder = PlaybackBinder()
    private var exoPlayer: ExoPlayer? = null
    private var wakeLock: PowerManager.WakeLock? = null

    private var currentTitle: String? = null
    private var currentDescription: String? = null
    private var currentDuration: Long? = null

    private var pendingStartForegroundAction: (() -> Unit)? = null

    private val playbackListener = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            when (playbackState) {
                Player.STATE_ENDED -> {
                    DebugLog.d("PlaybackService", "playbackState = STATE_ENDED")
                    stopForeground(true)
                    stopSelf()
                }
                else -> {
                    DebugLog.d("PlaybackService", "playbackState = $playbackState")
                    /** do nothing */
                }
            }
        }
    }

    private val mediaDescriptionAdapter by lazy {
        object : PlayerNotificationManager.MediaDescriptionAdapter {
            override fun createCurrentContentIntent(player: Player): PendingIntent? {
                val intent = Intent(this@PlaybackService, MainActivity::class.java)

                val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                } else {
                    PendingIntent.FLAG_UPDATE_CURRENT
                }

                return PendingIntent.getActivity(
                    this@PlaybackService,
                    0,
                    intent,
                    flags
                )
            }

            override fun getCurrentContentText(player: Player): String {
                return currentDescription ?: ""
            }

            override fun getCurrentContentTitle(player: Player): String {
                return currentTitle ?: ""
            }

            // TODO add bitmap
            override fun getCurrentLargeIcon(
                player: Player,
                callback: PlayerNotificationManager.BitmapCallback
            ): Bitmap? {
                return null
            }
        }
    }

    private val notificationListener by lazy {
        object : PlayerNotificationManager.NotificationListener {
            override fun onNotificationPosted(notificationId: Int, notification: Notification, onGoing: Boolean) {
                DebugLog.d("PlaybackService", "onNotificationPosted = $notificationId")
                pendingStartForegroundAction = { startForeground(notificationId, notification) }
            }

            override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
                DebugLog.d("PlaybackService", "onNotificationCancelled")
                stopSelf()
            }
        }
    }

    private val playerNotificationManager by lazy {
        PlayerNotificationManager.Builder(this, NOTIFICATION_ID, CHANNEL_ID)
            .setChannelNameResourceId(R.string.channel_name)
            .setMediaDescriptionAdapter(mediaDescriptionAdapter)
            .setNotificationListener(notificationListener)
            .build()
    }

    override fun onBind(intent: Intent): IBinder? {
        DebugLog.d("PlaybackService", "onBind")
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        intent.run {
            val url = getStringExtra(EXTRA_LECTURE_URL)
            currentTitle = getStringExtra(EXTRA_LECTURE_TITLE)
            currentDescription = getStringExtra(EXTRA_LECTURE_DESCRIPTION)
            currentDuration = getLongExtra(EXTRA_LECTURE_DURATION, 0L)
            DebugLog.d("PlaybackService", "$currentTitle")

            url?.let { playLecture(it) }
        }

        return START_REDELIVER_INTENT
    }

    override fun onCreate() {
        super.onCreate()
        DebugLog.d("PlaybackService", "onCreate")
        exoPlayer = ExoPlayer.Builder(this)
            .build().apply { addListener(playbackListener) }
        playerNotificationManager.setPlayer(exoPlayer)
    }

    override fun onDestroy() {
        DebugLog.d("PlaybackService", "onDestroy")
        playerNotificationManager.setPlayer(null)
        exoPlayer?.release()
        exoPlayer = null
        releaseWakeLock()
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
        exoPlayer?.apply {
            setMediaItem(MediaItem.fromUri(url))
            prepare()
            playWhenReady = true
            requireWakeLock()
        }
    }

    private fun requireWakeLock() =
        currentDuration?.let {
            try {
                val manager = getSystemService(POWER_SERVICE) as? PowerManager
                wakeLock = manager?.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, application.packageName)
                wakeLock?.acquire(it)
                DebugLog.d("PlaybackService", "requireWakeLock")
            } catch (e: Throwable) {
                DebugLog.e("PlaybackService", "requireWakeLock error", e)
                /** do nothing */
            }
        }

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