package com.prabhupadalectures.lectures.repository

import co.touchlab.stately.freeze
import com.prabhupadalectures.common.database.Database
import com.prabhupadalectures.common.network_api.ApiModel
import com.prabhupadalectures.common.network_api.PAGE_QUERY_KEY
import com.prabhupadalectures.common.network_api.PrabhupadaApi
import com.prabhupadalectures.common.network_api.QueryParams
import com.prabhupadalectures.lectures.data.*
import com.prabhupadalectures.lectures.data.filters.Filter
import com.prabhupadalectures.lectures.data.lectures.Lecture
import com.prabhupadalectures.lectures.events.Play
import com.prabhupadalectures.lectures.events.SeekTo
import com.prabhupadalectures.lectures.events.SliderReleased
import com.prabhupadalectures.lectures.utils.ShareAction
import io.github.aakira.napier.Napier
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ResultsState(
    val isLoading: Boolean = false,
    val filters: List<Filter> = emptyList(),
    val lectures: List<Lecture> = emptyList(),
    val pagination: Pagination = Pagination(),
)

interface ResultsRepository {
    fun observeState(): Flow<ResultsState>

    suspend fun init(shareAction: ShareAction?): Unit?
    suspend fun updatePage(page: Int)
    suspend fun updateQuery(queryParam: QueryParam)
    suspend fun clearAllFilters()
    fun capturePlayback()
    fun queryParams(): String?

    suspend fun getResults(page: Int): ApiModel
}

class ResultsRepositoryImpl(
    private val db: Database,
    private val api: PrabhupadaApi,
    private val playbackRepository: PlaybackRepository,
    withLog: Boolean
) :
    CoroutineScope by CoroutineScope(Dispatchers.Default), ResultsRepository {

    private var shareAction: ShareAction? = null
    private val state = MutableStateFlow(ResultsState())
    private val currentPage
        get() = state.value.pagination.curr

    init {
//        if (withLog) Napier.base(DebugAntilog())

//        observeDownloads()
//        observeFavorites()
//        observeCompleted()
//        observeSelfState()
    }

    override fun observeState() = state.asStateFlow()

    override suspend fun init(shareAction: ShareAction?) {
        this.shareAction = shareAction

        val queryParams =
            shareAction?.run { queryParams.toQueryParamsMap() }
                ?: settings
                    .getQueryParams()
                    .addPage(settings.getPage())

        loadMore(queryParams)
    }

    override suspend fun updatePage(page: Int) =
        loadMore(buildQueryParams(page = page))

    override suspend fun updateQuery(queryParam: QueryParam) =
        loadMore(queryParam)

    override suspend fun clearAllFilters() =
        loadMore(buildQueryParams(db = db, queryParam = QueryParam()))

    override fun capturePlayback() =
        playbackRepository.updatePlaylist(state.value.lectures)

    override fun queryParams(): String? {
        val list = mutableListOf<String>()
        if (currentPage > 1) list += "$PAGE_QUERY_KEY$LATEST_KEY_VALUE_SEPARATOR$currentPage"
        settings.getQueryParamsAsString()?.let { list += it }

        return list.joinToString(separator = LATEST_FILTERS_SEPARATOR) { it }.ifEmpty { null }
    }

    private suspend fun loadMore(queryParam: QueryParam?) =
        loadMore(buildQueryParams(queryParam))

    override suspend fun getResults(page: Int): ApiModel =
        api.getResults(page)

    private suspend fun loadMore(queryParams: QueryParams) {
        if (state.value.isLoading) {
            Napier.d("loadMore canceled, isLoading = true!", tag = "ResultsRepository")
            return
        }

        updateLoading(true)

        val result = api.getResults(queryParams)
        if (result.isSuccess) {
            result.getOrNull()?.let {
                updateData(it)
            } ?: Unit
        } else {
            Napier.e(message = "api.getResults isFailure", throwable = result.exceptionOrNull())
            updateLoading(false)
        }
    }

    private fun updateData(apiModel: ApiModel) {
        val newState = ResultsState(
            isLoading = false,
            filters = ApiMapper.filters(apiModel).updateFiltersFromDB(),
            lectures = state.value.lectures + ApiMapper.lectures(apiModel).updateLecturesFromDB(),
            pagination = Pagination(apiModel),
        )

        state.value = newState.freeze()

        val queryParams = buildQueryParams().toQueryParamsStringWithoutPage()

        settings.saveQueryParams(queryParams)
        settings.savePage(currentPage)

        db.insertPage(queryParams.hashCode().toLong(), currentPage)
    }

    private fun updateLoading(loading: Boolean) =
        updateStateIfNeeded(loading = loading)

    private fun updateFromDb() =
        updateStateIfNeeded(lectures = state.value.lectures.updateLecturesFromDB())

    private fun updateStateIfNeeded(
        loading: Boolean = state.value.isLoading,
        lectures: List<Lecture> = state.value.lectures
    ) {
        if (loading != state.value.isLoading || lectures != state.value.lectures) {
            state.value = state.value.run {
                copy(
                    isLoading = loading,
                    lectures = lectures
                )
            }
        }
    }

    private fun List<Lecture>.updateLecturesFromDB() =
        map { lecture ->
            val lectureEntity = db.selectLecture(lecture.id)
            lecture.copy(
                fileUrl = lectureEntity?.fileUrl ?: lecture.fileUrl,
                isFavorite = lectureEntity?.isFavorite ?: lecture.isFavorite,
                isCompleted = lectureEntity?.isCompleted ?: lecture.isCompleted,
                downloadProgress = lectureEntity?.downloadProgress?.toInt() ?: lecture.downloadProgress
            )
        }

    private fun List<Filter>.updateFiltersFromDB() =
        map { filter ->
            filter.copy(isExpanded = db.selectExpandedFilter(filter.name))
        }

    private fun buildQueryParams(queryParam: QueryParam? = null, page: Int = currentPage) =
        buildQueryParams(db, state.value.filters, page, queryParam)

    private fun observeDownloads() =
        launch {
            db.observeAllDownloads().collect {
                updateFromDb()
            }
        }

    private fun observeFavorites() =
        launch {
            db.observeAllFavorites().collect {
                updateFromDb()
            }
        }

    private fun observeCompleted() =
        launch {
            db.observeCompleted().collect {
                updateFromDb()
            }
        }

    private fun observeSelfState() =
        launch {
            observeState().collect {
                playbackRepository.updatePlaylist(it.lectures)
                shareAction?.run {
                    playbackRepository.run {
                        delay(200L)
                        handleAction(Play(lectureId))
                        if (timeMs != null) {
                            delay(200L)
                            handleAction(SeekTo(timeMs))
                            handleAction(SliderReleased)
                        }
                    }
                }
                shareAction = null
            }
        }
}
