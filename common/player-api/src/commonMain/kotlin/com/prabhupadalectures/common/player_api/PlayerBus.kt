package com.prabhupadalectures.common.player_api

import com.prabhupadalectures.common.lectures_api.Lecture
import kotlinx.coroutines.CoroutineScope

interface PlayerBus {
    fun update(state: PlayerState)
    fun update(playlist: List<Lecture>)
    fun update(action: PlayerAction)

    fun currentState(): PlayerState
    fun observeState(onEach: (PlayerState) -> Unit)
    fun observePlaylist(onEach: (List<Lecture>) -> Unit)
    fun observeActions(onEach: (PlayerAction) -> Unit)
}