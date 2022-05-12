package com.prabhupadalectures.common.player_api

import com.prabhupadalectures.common.lectures_api.Lecture
import kotlinx.coroutines.CoroutineScope

interface PlayerBus {
    fun update(state: PlayerState)
    fun update(playlist: List<Lecture>)
    fun update(action: PlayerAction)

    fun currentState(): PlayerState
    fun observeState(scope: CoroutineScope, onEach: (PlayerState) -> Unit)
    fun observePlaylist(scope: CoroutineScope, onEach: suspend (List<Lecture>) -> Unit)
    fun observeActions(scope: CoroutineScope, onEach: (PlayerAction) -> Unit)
}