package com.anadi.prabhupadalectures.android.player

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.MediaMetadata
import android.os.Build
import android.os.IBinder
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.widget.RemoteViews
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.anadi.prabhupadalectures.android.DebugLog
import com.anadi.prabhupadalectures.android.PrabhupadaApp.Companion.app
import com.anadi.prabhupadalectures.android.R
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect

enum class ServiceAction(
    val code: Int,
    @DrawableRes val icon: Int,
    val displayName: String,
    val actionName: String
) {
    PLAY(1, R.drawable.ic_player_play, "Play", "ACTION_SERVICE_PLAY"),
    PAUSE(2, R.drawable.ic_player_pause, "Pause", "ACTION_SERVICE_PAUSE"),
    STOP(3, R.drawable.ic_player_pause, "Stop", "ACTION_SERVICE_STOP"),
    PREV(4, R.drawable.ic_player_prev, "Prev", "ACTION_SERVICE_PREV"),
    NEXT(5, R.drawable.ic_player_next, "Next", "ACTION_SERVICE_NEXT"),
    SEEK_BACK(6, R.drawable.ic_player_seek_backward, "-10sec", "ACTION_SERVICE_SEEK_BACK"),
    SEEK_FORWARD(7, R.drawable.ic_player_seek_forward, "+10sec", "ACTION_SERVICE_SEEK_FORWARD")
}

private const val TRACK_DURATION_MS = 60_000L

class TimerService : Service() {

    val backgroundScope = CoroutineScope(SupervisorJob() + Dispatchers.Unconfined)

    private val timeRunInSeconds = MutableStateFlow(0L)
    private val timeRunInMillis = MutableStateFlow(0L)
    private val isTracking = MutableStateFlow(false)
    private val isFinish = MutableStateFlow(true)

    private val NOTIFICATION_CHANNEL_ID = "NOTIFICATION_CHANNEL_ID123"
    private val NOTIFICATION_CHANNEL_NAME = "NOTIFICATION_CHANNEL_NAME123"


    private var isFirstRun = true
    private var serviceKilled = false

    /**
     * Builder of the current notification
     */

    private fun action(serviceAction: ServiceAction) =
        serviceAction.run {
            NotificationCompat.Action(icon, displayName, pendingIntent(this))
        }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun pendingIntent(serviceAction: ServiceAction) =
        PendingIntent.getService(this, serviceAction.code, intent(serviceAction), flags)

    private val flags =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }

    private fun intent(serviceAction: ServiceAction) =
        Intent(this, TimerService::class.java).apply { action = serviceAction.actionName }

    private val baseNotification = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_logo)
        .setLargeIcon(BitmapFactory.decodeResource(app.resources, R.drawable.ic_favorite))
        .setAutoCancel(false)
        .setStyle(NotificationCompat.DecoratedCustomViewStyle())
        .setColor(Color.rgb(182, 93, 13))
        .setColorized(true)
//        .setContent()
//        .setCustomContentView(notificationView)

    private lateinit var curNotification: NotificationCompat.Builder

    override fun onCreate() {
        super.onCreate()

        val remoteViews = RemoteViews(packageName, R.layout.layout_notification_prabhupad)
        remoteViews.setTextViewText(R.id.text_title, "Бхагавад-Гита 2.12")
        remoteViews.setTextViewText(R.id.text_message, "9 марта, 1966, Нью-Йорк (США)")

        baseNotification.setCustomContentView(remoteViews)

        curNotification = baseNotification

        mediaSession = MediaSessionCompat(this, "tag").apply {
            setFlags(0)
            setRepeatMode(PlaybackStateCompat.REPEAT_MODE_ONE)
            setMetadata(
                MediaMetadataCompat.Builder()
                    .putLong(MediaMetadata.METADATA_KEY_DURATION, TRACK_DURATION_MS)
                    .build()
            )
        }

        backgroundScope.launch {
            isTracking.collect {
                updateNotificationTrackingState(it)
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                ServiceAction.PLAY.actionName -> {
                    if (isFirstRun) {
                        startForegroundService()
                        isFirstRun = false
                        serviceKilled = false
                    } else {
                        startTimer()
                    }
                }
                ServiceAction.PAUSE.actionName -> {
                    DebugLog.d("TimerService", "Paused Service")
                    pauseService()
                }
                ServiceAction.STOP.actionName -> {
                    DebugLog.d("TimerService", "Stopped service.")
                    killService()
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    /**
     * Starts this service as a foreground service and creates the necessary notification
     */
    private val NOTIFICATION_ID = 100012
    private fun startForegroundService() {
        DebugLog.d("TimerService", "TimerService started.")

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        startForeground(NOTIFICATION_ID, curNotification.build())

        startTimer()
        isTracking.value = true

        // updating notification

        backgroundScope.launch {
            timeRunInSeconds.collect {
                if (!serviceKilled) {
                    val notification = curNotification.setContentText("$it")
                    notificationManager.notify(NOTIFICATION_ID, notification.build())
                }
            }
        }
    }

    /**
     * Updates the action buttons of the notification
     */
    private fun updateNotificationTrackingState(isTracking: Boolean) {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        curNotification.javaClass.getDeclaredField("mActions").apply {
            isAccessible = true
            set(curNotification, ArrayList<NotificationCompat.Action>())
        }

        if (!serviceKilled) {
            curNotification = baseNotification.addActions(isTracking) // .setMediaStyle()
            notificationManager.notify(NOTIFICATION_ID, curNotification.build())
        }
    }

    private fun NotificationCompat.Builder.addActions(isTracking: Boolean) =
        apply {
            if (isTracking) {
                addAction(action(ServiceAction.PREV))
                addAction(action(ServiceAction.SEEK_BACK))
                addAction(action(ServiceAction.PAUSE))
                addAction(action(ServiceAction.SEEK_FORWARD))
                addAction(action(ServiceAction.NEXT))
            } else {
                addAction(action(ServiceAction.PREV))
                addAction(action(ServiceAction.SEEK_BACK))
                addAction(action(ServiceAction.PLAY))
                addAction(action(ServiceAction.SEEK_FORWARD))
                addAction(action(ServiceAction.NEXT))
            }
        }

    private fun NotificationCompat.Builder.setMediaStyle(mediaSession: MediaSessionCompat = updateMediaSession()) =
        apply {
//            setStyle(androidx.media.app.NotificationCompat.DecoratedMediaCustomViewStyle()
//                .setMediaSession(mediaSession.sessionToken)
//                .setShowActionsInCompactView(2,3,4)
//            )
            setStyle(androidx.media.app.NotificationCompat.MediaStyle()
                .setMediaSession(mediaSession.sessionToken)
                .setShowActionsInCompactView(2,3,4)
            )
        }

    lateinit var mediaSession: MediaSessionCompat

    private fun updateMediaSession(): MediaSessionCompat {
        val state =
            when {
                isTracking.value -> PlaybackStateCompat.STATE_PLAYING
                else -> PlaybackStateCompat.STATE_PAUSED
            }

        mediaSession.setPlaybackState(
            PlaybackStateCompat.Builder()
                .setState(state, timeRunInSeconds.value, 1f)
                .build()
        )

        return mediaSession
    }

//    mediaSessionConnector.setQueueNavigator(new TimelineQueueNavigator(mediaSession) {
//        @Override
//        public MediaDescriptionCompat getMediaDescription(Player player, int windowIndex) {
//            Bundle extras = new Bundle();
//            extras.putInt(MediaMetadataCompat.METADATA_KEY_DURATION, -1);
//
//            return new MediaDescriptionCompat.Builder()
//                .setMediaId(trackModel.mediaId)
//                .setIconBitmap(trackModel.bitmap)
//                .setTitle(trackModel.title)
//                .setDescription(trackModel.description)
//                .setExtras(extras)
//                .build();
//        }
//    });


    /**
     * Disables the timer and tracking.
     */
    private fun pauseService() {
        isTracking.value = false
        isFinish.value = false
    }

    /**
     * Stops the service properly.
     */
    private fun killService() {
        serviceKilled = true
        isFirstRun = true
        pauseService()
        stopForeground(true)
        stopSelf()
    }

    private var lapTime = 0L // time since we started the timer
    private var timeRun = 0L // total time of the timer
    private var timeStarted = 0L // the time when we started the timer
    private var lastSecondTimestamp = 0L

    /**
     * Starts the timer for the tracking.
     */
    private fun startTimer() {

        isTracking.value = true
        timeStarted = System.currentTimeMillis()

        CoroutineScope(Dispatchers.Main).launch {
            while (isTracking.value && timeRunInSeconds.value < TRACK_DURATION_MS / 1000) {
                // time difference between now and time started
                lapTime = System.currentTimeMillis() - timeStarted
                // post the new laptime
                timeRunInMillis.value = (timeRun + lapTime)
                // if a new second was reached, we want to update timeRunInSeconds, too
                if (timeRunInMillis.value >= lastSecondTimestamp + 1000L) {
                    timeRunInSeconds.value = (timeRunInSeconds.value + 1)
                    lastSecondTimestamp += 1000L
                }
                delay(1000L)
            }
            timeRun += lapTime
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            lightColor = Color.rgb(182, 93, 13)
        }

        notificationManager.createNotificationChannel(channel)
    }
}