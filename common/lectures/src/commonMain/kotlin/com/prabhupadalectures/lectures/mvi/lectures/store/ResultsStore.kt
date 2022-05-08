package com.prabhupadalectures.lectures.mvi.lectures.store

import com.arkivanov.mvikotlin.core.store.Store
import com.prabhupadalectures.lectures.data.Pagination
import com.prabhupadalectures.lectures.data.lectures.Lecture
import com.prabhupadalectures.lectures.mvi.lectures.store.ResultsStore.Intent
import com.prabhupadalectures.lectures.mvi.lectures.store.ResultsStore.State

internal interface ResultsStore : Store<Intent, State, Nothing> {

    sealed interface Intent {
        object LoadPrev : Intent
        object LoadNext : Intent
        object Pause : Intent
        object UpdateFilters: Intent
        data class Play(val id: Long) : Intent
        data class Download(val id: Long) : Intent
        data class Favorite(val id: Long, val isFavorite: Boolean) : Intent

        data class UpdatePage(val page: Int) : Intent
    }

    data class State(
        val isLoading: Boolean = false,
        val lectures: List<Lecture> = emptyList(),
        val pagination: Pagination = Pagination(),
    )
}

