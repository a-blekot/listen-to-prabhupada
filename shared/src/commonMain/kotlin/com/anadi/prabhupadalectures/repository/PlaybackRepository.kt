package com.anadi.prabhupadalectures.repository

import com.anadi.prabhupadalectures.data.lectures.Lecture
import kotlinx.coroutines.flow.*

data class PlaybackState(
    val lecture: Lecture = Lecture(),
    val isPlaying: Boolean = false,
    val isBuffering: Boolean = false,
    val hasNext: Boolean = false,
    val hasPrevious: Boolean = false,
    val timeMs: Long = 0L,
    val durationMs: Long = 1L
)

interface PlaybackRepository {

    fun updateState(value: PlaybackState)
    fun currentState(): PlaybackState
    fun observeState(): StateFlow<PlaybackState>

    fun updatePlaylist(playlist: List<Lecture>)
    fun observePlaylist(): StateFlow<List<Lecture>>

    fun observePlayerActions(): SharedFlow<PlayerAction>
    suspend fun handleAction(playerAction: PlayerAction)
}

class PlaybackRepositoryImpl : PlaybackRepository {

    private val playlistFlow = MutableStateFlow(emptyList<Lecture>())
    private val playbackFlow = MutableStateFlow(PlaybackState())
    private val playerActionFlow = MutableSharedFlow<PlayerAction>()


    override fun updateState(value: PlaybackState) {
        playbackFlow.value = value
    }

    override fun currentState() = playbackFlow.value

    override fun observeState() = playbackFlow.asStateFlow()

    override fun updatePlaylist(playlist: List<Lecture>) {
        playlistFlow.value = playlist
    }

    override fun observePlaylist(): StateFlow<List<Lecture>> = playlistFlow.asStateFlow()

    override fun observePlayerActions() = playerActionFlow.asSharedFlow()

    override suspend fun handleAction(playerAction: PlayerAction) =
        playerActionFlow.emit(playerAction)
}