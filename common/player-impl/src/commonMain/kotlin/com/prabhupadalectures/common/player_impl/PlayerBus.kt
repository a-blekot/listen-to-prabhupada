package com.prabhupadalectures.common.player_impl

import com.prabhupadalectures.common.lectures_api.Lecture
import com.prabhupadalectures.common.player_api.PlayerAction
import com.prabhupadalectures.common.player_api.PlayerBus
import com.prabhupadalectures.common.player_api.PlayerState
import com.prabhupadalectures.common.utils.dispatchers.DispatcherProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


class PlayerBusImpl(
    private val dispatchers: DispatcherProvider
) : PlayerBus {

    private val scope = CoroutineScope(dispatchers.main)

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

    override fun observeState(scope: CoroutineScope, onEach: (PlayerState) -> Unit) {
        playbackFlow
            .onEach { onEach(it) }
            .launchIn(scope)
    }

    override fun observePlaylist(scope: CoroutineScope, onEach: suspend (List<Lecture>) -> Unit) {
        playlistFlow
            .onEach { onEach(it) }
            .launchIn(scope)
    }

    override fun observeActions(scope: CoroutineScope, onEach: (PlayerAction) -> Unit) {
        playerActionFlow
            .onEach { onEach(it) }
            .launchIn(scope)
    }
}