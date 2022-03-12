package com.anadi.prabhupadalectures.android.player

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import com.anadi.prabhupadalectures.android.DebugLog
import com.anadi.prabhupadalectures.android.MainActivity
import com.anadi.prabhupadalectures.android.R
import com.anadi.prabhupadalectures.android.ui.compose.*
import com.anadi.prabhupadalectures.data.lectures.Lecture
import com.anadi.prabhupadalectures.datamodel.Playlist
import com.anadi.prabhupadalectures.repository.PlaybackState
import com.anadi.prabhupadalectures.repository.Repository
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import kotlinx.coroutines.*

private const val UPDATE_PLAYBACK_STATE_INTERVAL_MS = 1000L
private const val SAVE_POSITION_INTERVAL_SECONDS = UPDATE_PLAYBACK_STATE_INTERVAL_MS * 5 / 1000
private const val CHANNEL_ID = "PlaybackService_CHANNEL_16108"
const val NOTIFICATION_ID = 16108
const val SEEK_INCREMENT_MS = 10_000L

class Player(
    context: Context,
    private val repository: Repository,
    private val playerScope: CoroutineScope,
    private val listener: Listener
) {

    interface Listener {
        fun onFinished() {}
        fun onNotificationPosted(notification: Notification) {}
        fun onNotificationCancelled() {}
        fun onPlaybackStarted(timeLeft: Long) {}
        fun onIsPlayingChanged(isPlaying: Boolean) {}
        fun onTrackChanged(mediaItem: MediaItem?) {}
    }

    private val playbackListener = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            when (playbackState) {
                Player.STATE_ENDED -> {
                    DebugLog.d("PlaybackService", "playbackState = STATE_ENDED")
                    if (updateJob?.isActive == true) {
                        updateJob?.cancel()
                    }
                    listener.onFinished()
                }
                else -> {
                    /** do nothing */
                }
            }
        }

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            if (isPlaying) {
                launchUpdateJob()
            } else {
                updatePlaybackState()
            }
            listener.onIsPlayingChanged(isPlaying)
        }

        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            listener.onTrackChanged(mediaItem)
            DebugLog.d("PlaybackService", "onMediaItemTransition")
            mediaItem?.mediaId?.toLongOrNull()?.let { lectureId ->
                DebugLog.d("PlaybackService", "lectureId = $lectureId, pos = ${repository.getSavedPosition(lectureId)}")
                exoPlayer?.seekTo(repository.getSavedPosition(lectureId))
            }
            launchUpdateJob()
        }
    }

    private var exoPlayer: ExoPlayer? = ExoPlayer.Builder(context)
        .setSeekForwardIncrementMs(SEEK_INCREMENT_MS)
        .setSeekBackIncrementMs(SEEK_INCREMENT_MS)
        .build().apply {
            repeatMode = Player.REPEAT_MODE_OFF
            addListener(playbackListener)
        }

    private val mediaDescriptionAdapter by lazy {
        object : PlayerNotificationManager.MediaDescriptionAdapter {
            override fun createCurrentContentIntent(player: Player): PendingIntent? {
                val intent = Intent(context, MainActivity::class.java)

                val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                } else {
                    PendingIntent.FLAG_UPDATE_CURRENT
                }

                return PendingIntent.getActivity(context, 0, intent, flags)
            }

            override fun getCurrentContentText(player: Player) =
                exoPlayer?.mediaMetadata?.description.toString()

            override fun getCurrentContentTitle(player: Player) =
                exoPlayer?.mediaMetadata?.title.toString()

            override fun getCurrentLargeIcon(player: Player, callback: PlayerNotificationManager.BitmapCallback) =
                notificationBg
        }
    }

    private val notificationBg: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.prabhupada_speaking)

    private val notificationListener by lazy {
        object : PlayerNotificationManager.NotificationListener {
            override fun onNotificationPosted(notificationId: Int, notification: Notification, onGoing: Boolean) {
                DebugLog.d("PlaybackService", "onNotificationPosted = $notificationId")
                listener.onNotificationPosted(notification)
            }

            override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
                DebugLog.d("PlaybackService", "onNotificationCancelled")
                listener.onNotificationCancelled()
            }
        }
    }

    private val playerNotificationManager by lazy {
        PlayerNotificationManager.Builder(context, NOTIFICATION_ID, CHANNEL_ID)
            .setPlayActionIconResourceId(R.drawable.ic_player_play)
            .setPauseActionIconResourceId(R.drawable.ic_player_pause)
            .setNextActionIconResourceId(R.drawable.ic_player_next)
            .setPreviousActionIconResourceId(R.drawable.ic_player_prev)
            .setFastForwardActionIconResourceId(R.drawable.ic_player_seek_forward)
            .setRewindActionIconResourceId(R.drawable.ic_player_seek_backward)
            .setChannelNameResourceId(R.string.channel_name)
            .setMediaDescriptionAdapter(mediaDescriptionAdapter)
            .setNotificationListener(notificationListener)
            .build().apply {
                setUsePlayPauseActions(true)
                setUseNextAction(true)
                setUsePreviousAction(true)
                setUseFastForwardAction(true)
                setUseRewindAction(true)
                setUseChronometer(true)
                setColorized(true)
                setColor(Color.rgb(182, 93, 13))
            }
    }

    private var currentPlaylist = Playlist()
    private var pendingPlaylist = Playlist()

    private fun updatePlaybackState() {
        val newState =
            exoPlayer?.run {
                PlaybackState(
                    lectureId = currentMediaItem?.mediaId?.toLongOrNull() ?: 0L,
                    title = mediaMetadata.title?.toString() ?: "",
                    description = mediaMetadata.description?.toString() ?: "",
                    isPlaying = isPlaying,
                    hasNext = hasNextMediaItem(),
                    hasPrevious = hasPreviousMediaItem(),
                    timeMs = currentPosition,
                    durationMs = duration
                )
            } ?: PlaybackState()

        repository.setPlaybackState(newState)
    }

    private fun saveCurrentPosition() =
        exoPlayer?.run {
            repository.savePosition(
                lectureId = currentMediaItem?.mediaId?.toLongOrNull() ?: 0L,
                timeMs = currentPosition
            )
        }

    private var updateJob: Job? = null

    val isPlaying
        get() = exoPlayer?.isPlaying == true

    var counter = 0L

    fun launchUpdateJob() {
        if (updateJob?.isActive == true) {
            updateJob?.cancel()
        }
        updateJob = playerScope.launch {
            while (true) {
                this@Player.updatePlaybackState()

                if ((counter++ % SAVE_POSITION_INTERVAL_SECONDS) == 0L) {
                    this@Player.saveCurrentPosition()
                }

                delay(UPDATE_PLAYBACK_STATE_INTERVAL_MS)
            }
        }
    }

    fun release() {
        hideNotification()
        exoPlayer?.release()
        exoPlayer = null
    }

    fun handleAction(uiAction: UIAction) =
        when (uiAction) {
            is Play -> play(uiAction.lectureId)
            Next -> exoPlayer?.seekToNextMediaItem()
            Prev -> exoPlayer?.seekToPreviousMediaItem()
            SeekForward -> exoPlayer?.seekForward()
            SeekBack -> exoPlayer?.seekBack()
            else -> {
                /** do nothing */
            }
        }

    suspend fun setPlaylist(playlist: Playlist) =
        withContext(Dispatchers.Main) {
            pendingPlaylist = playlist

            if (currentPlaylist.isEmpty()) {
                currentPlaylist = pendingPlaylist
                resetTracks(currentPlaylist.lectures)
            }
        }

    fun showNotification() =
        playerNotificationManager.setPlayer(exoPlayer)

    fun hideNotification() =
        playerNotificationManager.setPlayer(null)

    private fun resetTracks(lectures: List<Lecture>) =
        exoPlayer?.apply {
            playWhenReady = false
            setMediaItems(lectures.map { it.toMediaItem() })
            prepare()
        }

    private fun play(lectureId: Long) =
        exoPlayer?.run {
            playWhenReady = false

            if (pendingPlaylist.any { it.id == lectureId }) {
                currentPlaylist = pendingPlaylist
                resetTracks(currentPlaylist.lectures)
            }

            val index = currentPlaylist.lectures.indexOfFirst { it.id == lectureId }
            if (index >= 0 && index < currentPlaylist.lectures.size) {
                try {
                    exoPlayer?.seekTo(index, repository.getSavedPosition(lectureId))
                } catch (e: IllegalSeekPositionException) {

                }
            }

            playWhenReady = true
        }

    private fun Lecture.toMediaItem(): MediaItem {
        val metaData = MediaMetadata.Builder()
            .setDescription(displayedDescription)
            .setTitle(displayedTitle)
            .build()

        return MediaItem.Builder()
            .setUri(fileInfo.mediaStreamUrl)
            .setMediaId("$id")
            .setMediaMetadata(metaData)
            .build()
    }
}