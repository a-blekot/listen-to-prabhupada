package com.anadi.prabhupadalectures.android

import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import com.anadi.prabhupadalectures.android.PrabhupadaApp.Companion.app
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerNotificationManager

private const val NOTIFICATION_ID = 16108
private const val CHANNEL_ID = "PlaybackService_CHANNEL_16108"

object Player {

    private val orangeViewColor = Color.rgb(182, 93, 13)

    private var exoPlayer: ExoPlayer? = null

    private var currentTitle: String? = null
    private var currentDescription: String? = null
    private var currentDuration: Long? = null

    private val playbackListener = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            when (playbackState) {
                Player.STATE_ENDED -> {
                    DebugLog.d("PlaybackService", "playbackState = STATE_ENDED")
//                    stopForeground(true)
//                    stopSelf()
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
                val intent = Intent(app, MainActivity::class.java)

                val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                } else {
                    PendingIntent.FLAG_UPDATE_CURRENT
                }

                return PendingIntent.getActivity(
                    app,
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
//                pendingStartForegroundAction = { startForeground(notificationId, notification) }
            }

            override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
                DebugLog.d("PlaybackService", "onNotificationCancelled")
//                stopSelf()
            }
        }
    }

    private val playerNotificationManager by lazy {
        PlayerNotificationManager.Builder(app, NOTIFICATION_ID, CHANNEL_ID)
            .setChannelNameResourceId(R.string.channel_name)
            .setMediaDescriptionAdapter(mediaDescriptionAdapter)
            .setNotificationListener(notificationListener)
            .build().apply {
                setColorized(true)
                setColor(Color.rgb(182, 93, 13))
                setSmallIcon(R.drawable.ic_play);
                setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
            }
    }
}