package com.prabhupadalectures.common.player_impl

import com.prabhupadalectures.common.lectures_api.Lecture
import com.prabhupadalectures.common.player_api.PlayerAction
import com.prabhupadalectures.common.player_api.PlayerBus
import com.prabhupadalectures.common.player_api.PlayerState
import com.prabhupadalectures.common.utils.dispatchers.DispatcherProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus


class PlayerBusImpl(
    dispatchers: DispatcherProvider
) : PlayerBus {

    private val scope = CoroutineScope(dispatchers.main) + SupervisorJob()

    private val playbackFlow = MutableStateFlow(PlayerState())
    private val playlistFlow = MutableStateFlow(emptyList<Lecture>())
    private val playerActionFlow = MutableSharedFlow<PlayerAction>()

    override fun update(state: PlayerState) =
        playbackFlow.update { state }

    override fun update(playlist: List<Lecture>) =
        playlistFlow.update { playlist }

    override fun update(action: PlayerAction) {
        scope.launch { playerActionFlow.emit(action) }
    }

    override fun currentState() = playbackFlow.value

    override fun observeState(onEach: (PlayerState) -> Unit) {
        playbackFlow
            .onEach { onEach(it) }
            .launchIn(scope)
    }

    override fun observePlaylist(onEach: (List<Lecture>) -> Unit) {
        playlistFlow
            .onEach { onEach(it) }
            .launchIn(scope)
    }

    override fun observeActions(onEach: (PlayerAction) -> Unit) {
        playerActionFlow
            .onEach { onEach(it) }
            .launchIn(scope)
    }
}