package com.anadi.prabhupadalectures.repository

import com.anadi.prabhupadalectures.datamodel.QueryParam
import com.anadi.prabhupadalectures.network.api.QueryParams
import kotlinx.coroutines.flow.StateFlow

interface Repository {
    suspend fun updateQuery(queryParam: QueryParam): Unit?
    suspend fun init(): Unit?
    suspend fun loadMore(queryParam: QueryParam? = null): Unit?
    suspend fun loadMore(queryParams: QueryParams): Unit?

    fun setPlaybackState(newState: PlaybackState)
    fun observeState(): StateFlow<State>
    fun observePlaybackState(): StateFlow<PlaybackState>
    fun addFavorite(id: Long)
    fun removeFavorite(id: Long)

    fun getSavedPosition(lectureId: Long): Long
}