package com.anadi.prabhupadalectures.repository

import co.touchlab.stately.freeze
import com.anadi.prabhupadalectures.data.*
import com.anadi.prabhupadalectures.data.filters.Filter
import com.anadi.prabhupadalectures.data.lectures.Lecture
import com.anadi.prabhupadalectures.events.Play
import com.anadi.prabhupadalectures.events.SeekTo
import com.anadi.prabhupadalectures.events.SliderReleased
import com.anadi.prabhupadalectures.network.api.ApiModel
import com.anadi.prabhupadalectures.network.api.PrabhupadaApi
import com.anadi.prabhupadalectures.network.api.QueryParams
import com.anadi.prabhupadalectures.utils.CommonFlow
import com.anadi.prabhupadalectures.utils.ShareAction
import com.anadi.prabhupadalectures.utils.asCommonFlow
import io.github.aakira.napier.Napier
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ResultsState(
    val isLoading: Boolean = false,
    val filters: List<Filter> = emptyList(),
    val lectures: List<Lecture> = emptyList(),
    val pagination: Pagination = Pagination(),
)

interface ResultsRepository {
    fun observeState(): CommonFlow<ResultsState>

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

    override fun observeState(): CommonFlow<ResultsState> = state.asStateFlow().asCommonFlow()

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
        loadMore(buildQueryParams(QueryParam(), emptyList(), FIRST_PAGE, db))

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
            val cachedLecture = db.selectCachedLecture(lecture.id)
            lecture.copy(
                fileUrl = cachedLecture?.fileUrl ?: lecture.fileUrl,
                isFavorite = cachedLecture?.isFavorite ?: lecture.isFavorite,
                isCompleted = cachedLecture?.isCompleted ?: lecture.isCompleted,
                downloadProgress = cachedLecture?.downloadProgress ?: lecture.downloadProgress
            )
        }

    private fun List<Filter>.updateFiltersFromDB() =
        map { filter ->
            filter.copy(isExpanded = db.selectExpandedFilter(filter.name))
        }

    private fun buildQueryParams(queryParam: QueryParam? = null, page: Int = currentPage) =
        buildQueryParams(queryParam, state.value.filters, page, db)

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
