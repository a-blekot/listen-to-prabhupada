package com.anadi.prabhupadalectures.android.player

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

}