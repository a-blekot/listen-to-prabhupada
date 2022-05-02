package com.prabhupadalectures.lectures.mvi.lectures

import com.arkivanov.decompose.value.Value
import com.prabhupadalectures.lectures.data.Pagination
import com.prabhupadalectures.lectures.data.QueryParam
import com.prabhupadalectures.lectures.data.filters.Filter
import com.prabhupadalectures.lectures.data.lectures.Lecture
import com.prabhupadalectures.lectures.events.PlayerAction

interface Results {

    val models: Value<Model>

    fun onLoadPrev()
    fun onLoadNext()
    fun onPage(page: Int)
    fun onQueryParam(queryParam: QueryParam)
    fun onPause()
    fun onPlay(id: Long)
    fun onDownload(id: Long)
    fun onFavorite(id: Long, isFavorite: Boolean)

    data class Model(
        val isLoading: Boolean,
        val lectures: List<Lecture>,
        val filters: List<Filter>,
        val pagination: Pagination,
    )

    sealed class Output {
        data class Selected(val id: Long) : Output()
    }
}