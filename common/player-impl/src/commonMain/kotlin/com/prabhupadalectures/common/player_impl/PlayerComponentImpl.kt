package com.prabhupadalectures.common.player_impl

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.prabhupadalectures.common.lectures_api.Lecture
import com.prabhupadalectures.common.player_api.PlayerAction
import com.prabhupadalectures.common.player_api.PlayerBus
import com.prabhupadalectures.common.player_api.PlayerComponent
import com.prabhupadalectures.common.player_api.PlayerState
import kotlinx.coroutines.CoroutineScope

class PlayerComponentImpl(
    componentContext: ComponentContext,
    private val playerBus: PlayerBus,
    scope: CoroutineScope
) : PlayerComponent, ComponentContext by componentContext {

    private val mutableState = MutableValue(PlayerState())

    init {
        playerBus.observeState(scope) {
            mutableState.value = it
        }
    }

    override val flow: Value<PlayerState> = mutableState

    override fun onPause() = playerBus.update(PlayerAction.Pause)
    override fun onNext() = playerBus.update(PlayerAction.Next)
    override fun onPrev() = playerBus.update(PlayerAction.Prev)
    override fun onSeekForward() = playerBus.update(PlayerAction.SeekForward)
    override fun onSeekBack() = playerBus.update(PlayerAction.SeekBack)
    override fun onSliderReleased() = playerBus.update(PlayerAction.SliderReleased)
    override fun onPlay(id: Long) = playerBus.update(PlayerAction.Play(id))
    override fun onSeekTo(timeMs: Long) = playerBus.update(PlayerAction.SeekTo(timeMs))
    override fun onSpeed(speed: Float) = playerBus.update(PlayerAction.Speed(speed))
    override fun onDownload(lecture: Lecture) = playerBus.update(PlayerAction.Download(lecture))
}