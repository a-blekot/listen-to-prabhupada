package com.prabhupadalectures.common.lectures_impl.mvi.store

import com.arkivanov.mvikotlin.core.store.Store
import com.prabhupadalectures.common.lectures_api.Lecture

import com.prabhupadalectures.common.lectures_api.LecturesState
import com.prabhupadalectures.common.lectures_impl.mvi.store.LecturesStore.Intent
import com.prabhupadalectures.common.lectures_impl.mvi.store.LecturesStore.Label

internal interface LecturesStore : Store<Intent, LecturesState, Label> {

    sealed interface Intent {
        object UpdateFilters: Intent
        data class CurrentLecture(val id: Long, val isPlaying: Boolean) : Intent
        data class Favorite(val id: Long, val isFavorite: Boolean) : Intent
        data class UpdatePage(val page: Int) : Intent
    }

    sealed interface Label {
        data class LecturesLoaded(val lectures: List<Lecture>) : Label
    }
}

