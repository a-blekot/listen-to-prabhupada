package com.anadi.prabhupadalectures.repository

import com.anadi.prabhupadalectures.data.Database
import com.anadi.prabhupadalectures.data.filters.Filter
import com.anadi.prabhupadalectures.data.lectures.Lecture
import com.anadi.prabhupadalectures.datamodel.ApiMapper
import com.anadi.prabhupadalectures.datamodel.Pagination
import com.anadi.prabhupadalectures.datamodel.QueryParam
import com.anadi.prabhupadalectures.datamodel.buildQueryParams
import com.anadi.prabhupadalectures.network.api.ApiModel
import com.anadi.prabhupadalectures.network.api.PrabhupadaApi
import com.anadi.prabhupadalectures.network.api.QueryParams
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.Serializable

data class ResultsState(
    val isLoading: Boolean = false,
    val lecturesCount: Int = 0,
    val filters: List<Filter> = emptyList(),
    val lectures: List<Lecture> = emptyList(),
    val pagination: Pagination = Pagination(),
)

interface ResultsRepository {
    fun observeState(): StateFlow<ResultsState>

    suspend fun init(): Unit?
    suspend fun updatePage(page: Int)
    suspend fun updateQuery(queryParam: QueryParam)
}

class ResultsRepositoryImpl(
    private val db: Database,
    private val api: PrabhupadaApi,
    withLog: Boolean
) :
    CoroutineScope by CoroutineScope(Dispatchers.Main), ResultsRepository, Serializable {

    private val state = MutableStateFlow(ResultsState())
    private val currentPage
        get() = state.value.pagination.curr

    init {
        if (withLog) Napier.base(DebugAntilog())

        observeDownloads()
        observeFavorites()
    }

    override fun observeState(): StateFlow<ResultsState> = state

    override suspend fun init() {
        loadMore(
            settings
                .getQueryParams()
                .addPage(settings.getPage())
        )
    }

    override suspend fun updatePage(page: Int) =
        loadMore(buildQueryParams(page = page))

    override suspend fun updateQuery(queryParam: QueryParam) =
        loadMore(queryParam)

    private suspend fun loadMore(queryParam: QueryParam?) =
        loadMore(buildQueryParams(queryParam))

    private suspend fun loadMore(queryParams: QueryParams) = withContext(Dispatchers.Default) {
        updateLoading(true)

        Napier.d("loadMore $queryParams", tag = "PAGE_DB")

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
            lecturesCount = apiModel.count,
            filters = ApiMapper.filters(apiModel).updateFiltersFromDB(),
            lectures = ApiMapper.lectures(apiModel).updateLecturesFromDB(),
            pagination = Pagination(apiModel),
        )

        state.value = newState

        val queryParams = buildQueryParams().toQueryParamsStringWithoutPage()

        Napier.d("loadMore $queryParams", tag = "PAGE_DB")

        settings.saveQueryParams(queryParams)
        settings.savePage(currentPage)

        Napier.d("save: id=${queryParams.hashCode().toLong()}, page=${currentPage}", tag = "PAGE_DB")
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
                isFavorite = cachedLecture?.isFavorite == true || lecture.isFavorite,
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
}