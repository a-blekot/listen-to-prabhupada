package com.anadi.prabhupadalectures.android.ui.screens.results

import com.anadi.prabhupadalectures.android.viewmodel.UiEffect
import com.anadi.prabhupadalectures.android.viewmodel.UiEvent
import com.anadi.prabhupadalectures.android.viewmodel.UiState
import com.anadi.prabhupadalectures.data.lectures.Lecture
import com.anadi.prabhupadalectures.datamodel.QueryParam
import com.anadi.prabhupadalectures.repository.PlaybackState
import com.anadi.prabhupadalectures.repository.PlayerAction
import com.anadi.prabhupadalectures.repository.ResultsState

sealed class ResultsEvent : UiEvent {
    object TappedBack : ResultsEvent()
    data class Favorite(val lecture: Lecture, val isFavorite: Boolean) : ResultsEvent()
    data class Expand(val filterName: String, val isExpanded: Boolean) : ResultsEvent()
    data class Option(val queryParam: QueryParam) : ResultsEvent()
    data class Page(val page: Int) : ResultsEvent()
    data class Player(val action: PlayerAction) : ResultsEvent()
}

data class ResultsScreenState(
    val results: ResultsState = ResultsState(),
    val playback: PlaybackState = PlaybackState()
) : UiState

sealed class ResultsEffect : UiEffect {
    data class Toast(val message: String) : ResultsEffect()
}