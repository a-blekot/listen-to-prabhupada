package com.anadi.prabhupadalectures.repository

import com.anadi.prabhupadalectures.data.lectures.Lecture
import kotlinx.coroutines.flow.*
import java.io.Serializable

data class PlaybackState(
    val lecture: Lecture = Lecture(),
    val isPlaying: Boolean = false,
    val hasNext: Boolean = false,
    val hasPrevious: Boolean = false,
    val timeMs: Long = 0L,
    val durationMs: Long = 1L
)

interface PlaybackRepository {
    fun observeState(): StateFlow<PlaybackState>
    fun currentState(): PlaybackState
    fun observePlayerActions(): SharedFlow<PlayerAction>
    fun updateState(value: PlaybackState)
    suspend fun handleAction(playerAction: PlayerAction)
}

class PlaybackRepositoryImpl : PlaybackRepository, Serializable {

    private val playbackFlow = MutableStateFlow(PlaybackState())
    private val playerActionFlow = MutableSharedFlow<PlayerAction>()

    override fun observeState() = playbackFlow.asStateFlow()

    override fun currentState() = playbackFlow.value

    override fun observePlayerActions() = playerActionFlow.asSharedFlow()

    override fun updateState(value: PlaybackState) {
        playbackFlow.value = value
    }

    override suspend fun handleAction(playerAction: PlayerAction) =
        playerActionFlow.emit(playerAction)
}