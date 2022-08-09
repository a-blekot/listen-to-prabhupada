package com.listentoprabhupada.common.lectures_impl.mvi.store

import com.arkivanov.mvikotlin.core.store.Store
import com.listentoprabhupada.common.data.Lecture

import com.listentoprabhupada.common.lectures_api.LecturesState
import com.listentoprabhupada.common.lectures_impl.mvi.store.LecturesStore.Intent
import com.listentoprabhupada.common.lectures_impl.mvi.store.LecturesStore.Label

internal interface LecturesStore : Store<Intent, LecturesState, Label> {

    sealed interface Intent {
        object UpdateLectures: Intent
        data class CurrentLecture(val id: Long, val isPlaying: Boolean) : Intent
        data class Favorite(val id: Long, val isFavorite: Boolean) : Intent
        data class UpdatePage(val page: Int) : Intent
    }

    sealed interface Label {
        data class LecturesLoaded(val lectures: List<Lecture>) : Label
    }
}

