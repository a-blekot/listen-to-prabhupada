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
    val loading: Boolean = false,
    val lecturesCount: Int = 0,
    val filters: List<Filter> = emptyList(),
    val lectures: List<Lecture> = emptyList()
)

interface ResultsRepository {
    fun observeState(): StateFlow<ResultsState>

    suspend fun init(): Unit?
    suspend fun updateQuery(queryParam: QueryParam): Unit?
}

class ResultsRepositoryImpl(
    private val db: Database,
    private val api: PrabhupadaApi,
    withLog: Boolean
) :
    CoroutineScope by CoroutineScope(Dispatchers.Main), ResultsRepository, Serializable {

    private val state = MutableStateFlow(ResultsState())
    private var pagination = Pagination()

    init {
        if (withLog) Napier.base(DebugAntilog())

        observeDownloads()
        observeFavorites()
    }

    override fun observeState(): StateFlow<ResultsState> = state

    override suspend fun init() =
        loadMore(settings.getFilters())

    override suspend fun updateQuery(queryParam: QueryParam) =
        loadMore(queryParam)

    private suspend fun loadMore(queryParam: QueryParam?) =
        loadMore(buildQueryParams(queryParam))

    private suspend fun loadMore(queryParams: QueryParams) = withContext(Dispatchers.Default) {
        updateLoading(true)

        val result = api.getResults(queryParams)
        if (result.isSuccess) {
            result.getOrNull()?.let {
                updatePagination(it)
                updateData(it)
            }
        } else {
            Napier.e(message = "api.getResults isFailure", throwable = result.exceptionOrNull())
            updateLoading(false)
        }
    }

    private fun updatePagination(apiModel: ApiModel) {
        pagination = Pagination(apiModel).copy(curr = pagination.next ?: 0)
    }

    private fun updateData(apiModel: ApiModel) {
        val newState = ResultsState(
            loading = false,
            lecturesCount = apiModel.count,
            filters = ApiMapper.filters(apiModel),
            lectures = ApiMapper.lectures(apiModel).updateFromDB()
        )

        state.value = newState
        settings.saveFilters(buildQueryParams())
    }

    private fun updateLoading(loading: Boolean) =
        updateStateIfNeeded(loading = loading)

    private fun updateFromDb() =
        updateStateIfNeeded(lectures = state.value.lectures.updateFromDB())

    private fun updateStateIfNeeded(
        loading: Boolean = state.value.loading,
        lectures: List<Lecture> = state.value.lectures
    ) {
        if (loading != state.value.loading || lectures != state.value.lectures) {
            state.value = state.value.run {
                copy(
                    loading = loading,
                    lectures = lectures
                )
            }
        }
    }

    private fun List<Lecture>.updateFromDB() =
        map { lecture ->
            val cachedLecture = db.selectCachedLecture(lecture.id)
            lecture.copy(
                fileUrl = cachedLecture?.fileUrl ?: lecture.fileUrl,
                isFavorite = cachedLecture?.isFavorite == true || lecture.isFavorite,
                downloadProgress = cachedLecture?.downloadProgress ?: lecture.downloadProgress
            )
        }

    private fun buildQueryParams(queryParam: QueryParam? = null) =
        buildQueryParams(queryParam, state.value.filters, pagination.curr)

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