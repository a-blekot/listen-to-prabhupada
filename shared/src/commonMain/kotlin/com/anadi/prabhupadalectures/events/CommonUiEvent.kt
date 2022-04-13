package com.anadi.prabhupadalectures.events

import com.anadi.prabhupadalectures.data.lectures.Lecture
import com.anadi.prabhupadalectures.data.QueryParam

interface UiState

interface UiEvent

interface UiEffect

sealed class CommonUiEvent : UiEvent {
    object TappedBack : CommonUiEvent()
    data class Favorite(val lecture: Lecture, val isFavorite: Boolean) : CommonUiEvent()
    data class Player(val action: PlayerAction) : CommonUiEvent()
    data class Share(val lectureId: Long) : CommonUiEvent()

    sealed class ResultsEvent : CommonUiEvent() {
        object OpenDownloads : ResultsEvent()
        object OpenFavorites : ResultsEvent()
        object OpenHelpTranslation : ResultsEvent()
        object ClearAllFilters : ResultsEvent()

        data class Expand(val filterName: String, val isExpanded: Boolean) : ResultsEvent()
        data class Option(val queryParam: QueryParam) : ResultsEvent()
        data class Page(val page: Int) : ResultsEvent()
    }

    sealed class FavoritesEvent : CommonUiEvent()
    sealed class DownloadsEvent : CommonUiEvent()
}