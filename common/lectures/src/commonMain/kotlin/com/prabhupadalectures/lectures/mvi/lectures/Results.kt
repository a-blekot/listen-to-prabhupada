package com.prabhupadalectures.lectures.mvi.lectures

import com.arkivanov.decompose.value.Value
import com.prabhupadalectures.lectures.data.Pagination
import com.prabhupadalectures.lectures.data.lectures.Lecture

interface Results {

    val models: Value<Model>

    fun onLoadPrev()
    fun onLoadNext()
    fun onPage(page: Int)
    fun onPause()
    fun onPlay(id: Long)
    fun onDownload(id: Long)
    fun onFavorite(id: Long, isFavorite: Boolean)
    fun onEditFilters()
    fun onUpdateFilters()

    data class Model(
        val isLoading: Boolean,
        val lectures: List<Lecture>,
        val pagination: Pagination,
    )

    sealed class Output {
        object EditFilters : Output()
    }
}