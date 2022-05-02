package com.prabhupadalectures.android.player

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import com.prabhupadalectures.android.MainActivity
import com.prabhupadalectures.android.R
import com.prabhupadalectures.android.util.notificationColor
import com.prabhupadalectures.lectures.data.lectures.Lecture
import com.prabhupadalectures.lectures.events.*
import com.prabhupadalectures.lectures.repository.*
import com.prabhupadalectures.lectures.utils.toValidUrl
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import io.github.aakira.napier.Napier
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach


private const val UPDATE_PLAYBACK_STATE_INTERVAL_MS = 1000L
private const val SAVE_POSITION_INTERVAL_SECONDS = UPDATE_PLAYBACK_STATE_INTERVAL_MS * 5 / 1000
private const val CHANNEL_ID = "PlaybackService_CHANNEL_16108"

const val NOTIFICATION_ID = 16108
const val SEEK_INCREMENT_MS = 10_000L

class Player(
    context: Context,
    private val playbackRepository: PlaybackRepository,
    private val tools: ToolsRepository,
    private val playerScope: CoroutineScope,
    private val listener: Listener
) {

    interface Listener {
        fun onNotificationPosted(notification: Notification) {}
        fun onNotificationCancelled() {}
    }

    private val Int.readablePlaybackState
        get() = when (this) {
            Player.STATE_IDLE -> "Player.STATE_IDLE"
            Player.STATE_BUFFERING -> "Player.STATE_BUFFERING"
            Player.STATE_READY -> "Player.STATE_READY"
            Player.STATE_ENDED -> "Player.STATE_ENDED"
            else -> "wrong state $this. Should be in range ${Player.STATE_IDLE}..${Player.STATE_ENDED}"
        }

    private val playbackListener = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            Napier.d("onPlaybackStateChanged ${playbackState.readablePlaybackState}", tag = "AUDIO_PLAYER")
            when (playbackState) {
                Player.STATE_ENDED -> {
                    stopUpdateJob()
                }
                else -> {
                    updatePlaybackState()
                    /** do nothing */
                }
            }
        }

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            if (isPlaying) {
                launchUpdateJob()
            } else {
                stopUpdateJob()
                updatePlaybackState()
                saveCurrentPosition()
            }
        }

        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            mediaItem?.mediaId?.toLongOrNull()?.let { lectureId ->
                exoPlayer?.seekTo(tools.getPosition(lectureId))
            }
            launchUpdateJob()
        }

        override fun onPlayerError(error: PlaybackException) {
            Napier.e("onPlayerError", error, tag = "AUDIO_PLAYER")
            Napier.e("current lecture = ${exoPlayer?.currentLecture}", tag = "AUDIO_PLAYER")
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
                Napier.d( "onNotificationPosted = $notificationId", tag = "AUDIO_PLAYER")
                listener.onNotificationPosted(notification)
            }

            override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
                Napier.d("onNotificationCancelled", tag = "AUDIO_PLAYER")
                listener.onNotificationCancelled()
            }
        }
    }

    private val playerNotificationManager by lazy {
        PlayerNotificationManager.Builder(context, NOTIFICATION_ID, CHANNEL_ID)
            .setSmallIconResourceId(R.drawable.ic_logo)
            .setStopActionIconResourceId(R.drawable.ic_player_close)
            .setPlayActionIconResourceId(R.drawable.ic_player_play)
            .setPauseActionIconResourceId(R.drawable.ic_player_pause)
            .setNextActionIconResourceId(R.drawable.ic_player_next)
            .setPreviousActionIconResourceId(R.drawable.ic_player_prev)
            .setFastForwardActionIconResourceId(R.drawable.ic_player_seek_forward)
            .setRewindActionIconResourceId(R.drawable.ic_player_seek_backward)
            .setChannelNameResourceId(R.string.playback_channel_name)
            .setMediaDescriptionAdapter(mediaDescriptionAdapter)
            .setNotificationListener(notificationListener)
            .build().apply {
                setUseStopAction(true)
                setUsePlayPauseActions(true)
                setUseNextAction(true)
                setUsePreviousAction(false)
                setUseFastForwardAction(true)
                setUseRewindAction(true)
                setUseFastForwardActionInCompactView(true)
                setUseRewindActionInCompactView(true)
                setUseChronometer(true)
                setColorized(true)
                setColor(notificationColor)
            }
    }

    private var currentPlaylist: List<Lecture> = emptyList()
    private var pendingPlaylist: List<Lecture> = emptyList()

    init {
        playerScope.launch {
            playbackRepository.observePlayerActions()
                .onEach { handleAction(it) }
                .collect()
        }

        playerScope.launch {
            playbackRepository.observePlaylist()
                .onEach { setPlaylist(it) }
                .collect()
        }
    }

    private fun updatePlaybackState(playbackState: PlaybackState = exoPlayer?.myPlaybackState ?: PlaybackState()) =
        playbackRepository.updateState(playbackState)

    private fun saveCurrentPosition() =
        exoPlayer?.run {
            if (duration < SAVE_POSITION_INTERVAL_SECONDS) {
                return@run
            }

            val timeMs = if (trackIsAlmostCompleted) {
                tools.setCompleted(currentId)
                0L
            } else {
                currentPosition
            }

            tools.savePosition(id = currentId, timeMs = timeMs)
        }

    private var updateCounter = 0L
    private var updateJob: Job? = null

    val isPlaying
        get() = exoPlayer?.isPlaying == true


    fun release() {
        hideNotification()
        exoPlayer?.release()
        exoPlayer = null
    }

    private fun handleAction(playerAction: PlayerAction) {
        Napier.d("handleAction $playerAction", tag = "AUDIO_PLAYER")

        when (playerAction) {
            is Play -> play(playerAction.lectureId)
            is SeekTo -> seekTo(playerAction.timeMs)
            Pause -> exoPlayer?.playWhenReady = false
            Next -> exoPlayer?.seekToNextMediaItem()
            Prev -> exoPlayer?.seekToPreviousMediaItem()
            SeekForward -> exoPlayer?.seekForward()
            SeekBack -> exoPlayer?.seekBack()
            SliderReleased -> onSliderReleased()
            else -> {
                /** do nothing */
            }
        }
    }


    private suspend fun setPlaylist(playlist: List<Lecture>) =
        withContext(Dispatchers.Main) {
            pendingPlaylist = playlist
            maybeUpdateCurrentPlaylist(playlist)
        }

    private fun maybeUpdateCurrentPlaylist(playlist: List<Lecture>) {
        if (currentPlaylist.isEmpty()) {
            currentPlaylist = playlist
            resetTracks(currentPlaylist)
            return
        }

        handleNewList(playlist)
    }

    private fun handleNewList(playlist: List<Lecture>) {
        val old = currentPlaylist.map { it.id }
        val new = playlist.map { it.id }

        when (old) {
            new -> currentPlaylist = playlist
            else -> checkRemovedItem(old, new)
        }
    }

    private fun checkRemovedItem(old: List<Long>, new: List<Long>) {
        if (old.isEmpty() || new.isEmpty()) return

        val removedIds = old.minus(new)
        if (removedIds.size == old.size) {
            return
        }
        removedIds.firstOrNull()?.let { removeMediaItem(old.indexOf(it)) }
    }

    private fun removeMediaItem(index: Int) =
        exoPlayer?.apply {
            if (currentPlaylist.isEmpty()) {
                return@apply
            }

            if (currentMediaItemIndex == index) {
                playWhenReady = false
            }

            if (index in 0..currentPlaylist.lastIndex) {
                currentPlaylist = currentPlaylist.filterIndexed { i, _ ->
                    i != index
                }
                removeMediaItem(index)
            }
        }

    fun showNotification() {
        Napier.d("showNotification", tag = "AUDIO_PLAYER")
        playerNotificationManager.setPlayer(exoPlayer)
    }

    fun hideNotification() {
        Napier.d("hideNotification", tag = "AUDIO_PLAYER")
        playerNotificationManager.setPlayer(null)
    }

    private fun resetTracks(lectures: List<Lecture>) =
        exoPlayer?.apply {
            playWhenReady = false
            setMediaItems(lectures.map { it.toMediaItem() })
            prepare()
        }

    private fun play(lectureId: Long) =
        exoPlayer?.run {
            playWhenReady = false

            if (currentId != lectureId) {
                switchTrack(lectureId)
            }

            playWhenReady = true
        }

    private fun switchTrack(lectureId: Long) {
        if (pendingPlaylist.any { it.id == lectureId }) {
            currentPlaylist = pendingPlaylist
            resetTracks(currentPlaylist)
        }

        val index = currentPlaylist.indexOfFirst { it.id == lectureId }
        if (index >= 0 && index < currentPlaylist.size) {
            try {
                exoPlayer?.seekTo(index, tools.getPosition(lectureId))
            } catch (e: IllegalSeekPositionException) {

            }
        }
    }

    private var playOnSliderRelease: Boolean? = null
    private var seekState: PlaybackState? = null
    private fun seekTo(timeMs: Long) =
        exoPlayer?.run {
            if (playOnSliderRelease == null) {
                playOnSliderRelease = isPlaying
                playWhenReady = false
                seekState = myPlaybackState
                stopUpdateJob()
            }

            seekState?.copy(timeMs = timeMs)?.let {
                updatePlaybackState(it)
            }

            try {
                exoPlayer?.seekTo(timeMs)
            } catch (e: IllegalSeekPositionException) {

            }
        }

    private fun onSliderReleased() {
        playOnSliderRelease?.let { exoPlayer?.playWhenReady = it }
        playOnSliderRelease = null
        seekState = null
    }

    private fun launchUpdateJob() {
        stopUpdateJob()
        updateJob = playerScope.launch {
            while (true) {
                this@Player.updatePlaybackState()

                if ((updateCounter++ % SAVE_POSITION_INTERVAL_SECONDS) == 0L) {
                    this@Player.saveCurrentPosition()
                }

                delay(UPDATE_PLAYBACK_STATE_INTERVAL_MS)
            }
        }
    }

    private fun stopUpdateJob() {
        if (updateJob?.isActive == true) {
            updateJob?.cancel()
            updateJob = null
        }
    }

    val totalDurationMillis
        get() = currentPlaylist.sumOf { it.durationMillis }

    private fun Lecture.toMediaItem(): MediaItem {
        val metaData = MediaMetadata.Builder()
            .setDescription(displayedDescription)
            .setTitle(title)
            .build()

        return MediaItem.Builder()
            .setUri(fileUrl?.ifEmpty { null } ?: remoteUrl.toValidUrl())
            .setMediaId("$id")
            .setMediaMetadata(metaData)
            .build()
    }

    private val ExoPlayer.myPlaybackState
        get() = PlaybackState(
            lecture = currentLecture,
            isPlaying = isPlaying,
            isBuffering = playbackState == Player.STATE_BUFFERING,
            hasNext = hasNextMediaItem(),
            hasPrevious = hasPreviousMediaItem(),
            timeMs = currentPosition,
            durationMs = duration
        )

    private val ExoPlayer.currentId
        get() = currentMediaItem?.mediaId?.toLongOrNull() ?: 0L

    private val ExoPlayer.currentLecture
        get() = currentPlaylist.firstOrNull { it.id == currentId } ?: Lecture()

    private val ExoPlayer.trackIsAlmostCompleted
        get() = duration - currentPosition < SAVE_POSITION_INTERVAL_SECONDS * 1000L

//    lectureId = currentMediaItem?.mediaId?.toLongOrNull() ?: 0L,
//    url = currentMediaItem?.localConfiguration?.uri?.toString() ?: "",
//    title = mediaMetadata.title?.toString() ?: "",
//    description = mediaMetadata.description?.toString() ?: "",
}