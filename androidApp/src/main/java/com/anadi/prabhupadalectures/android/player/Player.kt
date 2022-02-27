package com.anadi.prabhupadalectures.android.player

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import com.anadi.prabhupadalectures.android.DebugLog
import com.anadi.prabhupadalectures.android.MainActivity
import com.anadi.prabhupadalectures.android.R
import com.anadi.prabhupadalectures.android.ui.compose.*
import com.anadi.prabhupadalectures.data.lectures.Lecture
import com.anadi.prabhupadalectures.datamodel.Playlist
import com.anadi.prabhupadalectures.repository.PlaybackState
import com.anadi.prabhupadalectures.repository.Repository
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.IllegalSeekPositionException
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import kotlinx.coroutines.*

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
        fun onNotificationCancelled() {}
        fun onPlaybackStarted(timeLeft: Long) {}
        fun onIsPlayingChanged(isPlaying: Boolean) {}
        fun onTrackChanged(mediaItem: MediaItem?) {}
    }

    val notificationBg: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.prabhupada_speaking)

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
                    DebugLog.d("PlaybackService", "playbackState = $playbackState")
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
            launchUpdateJob()
        }
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
                playlist.currentLecture?.displayedDescription

            override fun getCurrentContentTitle(player: Player) =
                playlist.currentLecture?.displayedTitle ?: ""

            // TODO add bitmap
            override fun getCurrentLargeIcon(player: Player, callback: PlayerNotificationManager.BitmapCallback) =
                notificationBg
        }
    }

    private val notificationListener by lazy {
        object : PlayerNotificationManager.NotificationListener {
            override fun onNotificationPosted(notificationId: Int, notification: Notification, onGoing: Boolean) {
                DebugLog.d("PlaybackService", "onNotificationPosted = $notificationId")
                this@Player.notification = notification
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
            }
    }

    private var exoPlayer: ExoPlayer? = ExoPlayer.Builder(context)
        .setSeekForwardIncrementMs(SEEK_INCREMENT_MS)
        .setSeekBackIncrementMs(SEEK_INCREMENT_MS)
        .build().apply {
            repeatMode = Player.REPEAT_MODE_OFF
            addListener(playbackListener)
        }

    private var playlist = Playlist()

    var notification: Notification? = null

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

    private var updateJob: Job? = null

    fun launchUpdateJob() {
        if (updateJob?.isActive == true) {
            updateJob?.cancel()
        }
        updateJob = playerScope.launch {
            while (true) {
                this@Player.updatePlaybackState()
                delay(1000L)
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
            is Play -> switchTrack(uiAction.lecture, uiAction.isPlaying)
            is Pause -> switchTrack(uiAction.lecture, uiAction.isPlaying)
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
            if (this@Player.playlist.lectures != playlist.lectures) {
                resetTracks(playlist.lectures)
            }

            if (this@Player.playlist.currentIndex != playlist.currentIndex) {
                playlist.currentLecture?.let { switchTrack(it) }
            }

            if (this@Player.playlist.isPlaying != playlist.isPlaying) {
                exoPlayer?.playWhenReady = playlist.isPlaying
            }

            this@Player.playlist = playlist
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

    private fun Lecture.toMediaItem() =
        MediaItem.Builder()
            .setUri(fileInfo.mediaStreamUrl)
            .setMediaId("$id")
            .build()

    private fun switchTrack(lecture: Lecture, isPlaying: Boolean = exoPlayer?.isPlaying ?: false) =
        exoPlayer?.run {
            playWhenReady = false

            if (playlist.lectures.isEmpty()) return@run

            val index = playlist.lectures.indexOf(lecture)
            if (index >= 0 && index < playlist.lectures.size) {
                try {
                    exoPlayer?.seekTo(index, repository.getSavedPosition(lecture.id))
                } catch (e: IllegalSeekPositionException) {

                }
            }

            playWhenReady = isPlaying
        }
}