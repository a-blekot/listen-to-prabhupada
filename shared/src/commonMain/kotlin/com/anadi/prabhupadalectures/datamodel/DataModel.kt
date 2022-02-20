package com.anadi.prabhupadalectures.datamodel

import com.anadi.prabhupadalectures.data.Database
import com.anadi.prabhupadalectures.data.filters.Filter
import com.anadi.prabhupadalectures.data.lectures.Lecture
import com.anadi.prabhupadalectures.network.api.ApiModel
import com.anadi.prabhupadalectures.network.api.QueryParams
import com.anadi.prabhupadalectures.repository.Repository
import com.anadi.prabhupadalectures.repository.getFilters
import com.anadi.prabhupadalectures.repository.saveFilters
import com.anadi.prabhupadalectures.repository.settings
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext

data class State(
    val loading: Boolean,
    val filters: List<Filter> = emptyList(),
    val playlist: Playlist = Playlist()
)

class DataModel(
    private val db: Database,
    private val repository: Repository,
    withLog: Boolean
) :
    CoroutineScope by CoroutineScope(Dispatchers.Main) {

    private val state = MutableStateFlow(State(false))
    private var pagination = Pagination()

    init {
        if (withLog) Napier.base(DebugAntilog())
    }

    fun observeState(): StateFlow<State> = state

    private fun printFavorites() =
        db.getAllFavorites().forEach {
            Napier.d("favorite $it}")
        }

    fun addFavorite(id: Long) {
        db.setFavorite(id)
        refreshFavorites()
        printFavorites()
    }

    fun removeFavorite(id: Long) {
        db.removeFavorite(id)
        refreshFavorites()
        printFavorites()
    }

    suspend fun updateQuery(queryParam: QueryParam) =
        loadMore(queryParam)

    suspend fun init() =
        loadMore(settings.getFilters())

    suspend fun loadMore(queryParam: QueryParam? = null) =
        loadMore(buildQueryParams(queryParam))

    suspend fun loadMore(queryParams: QueryParams) = withContext(Dispatchers.Default) {
        setLoading(true)

        Napier.d("firstLoad: pagination = $pagination")
        val result = repository.getResults(queryParams)
        if (result.isSuccess) {
            result.getOrNull()?.let {
                updatePagination(it)
                updateData(it)
            }
        } else {
            Napier.e(message = "repository.getResults isFailure", tag = "DataModel", throwable = result.exceptionOrNull())
            setLoading(false)
        }
    }

    private fun updatePagination(apiModel: ApiModel) {
        pagination = Pagination(apiModel).copy(curr = pagination.next ?: 0)
    }

    private fun setLoading(loading: Boolean) {
        if (loading != state.value.loading) {
            state.value = state.value.copy(loading = loading)
        }
    }

    private fun updateData(apiModel: ApiModel) {
        val newState = State(
            loading = false,
            playlist = Playlist(ApiMapper.lectures(apiModel).updateFromDB()),
            filters = ApiMapper.filters(apiModel)
        )

        state.value = newState
        settings.saveFilters(buildQueryParams())
    }

    private fun refreshFavorites() {
        val lecturesWithDB = state.value.playlist.lectures.updateFromDB()
        val stateWithDB = state.value.copy(
            playlist = state.value.playlist.copy(lectures = lecturesWithDB)
        )

        if (stateWithDB != state.value) {
//            Napier.d(tag = "DataModel", message = "NewState: $newState")
            state.value = stateWithDB
        }
    }

    private fun List<Lecture>.updateFromDB() =
        map { lecture ->
            lecture.copy(
                isFavorite = db.isFavorite(lecture.id),
                isDownloaded = db.isDownloaded(lecture.id)
            )
        }

    private fun QueryParam?.unSelected(option: String) =
        this != null && !isSelected && selectedOption == option

    private fun buildQueryParams(queryParam: QueryParam? = null): HashMap<String, Any> {
        val params = HashMap<String, Any>().apply {
            put("page", pagination.curr.coerceAtLeast(1))

            if (queryParam?.isSelected == true) {
                put(queryParam.filterName, queryParam.selectedOption)
            }
        }

        state.value.filters.forEach { filter ->
            filter.options.firstOrNull { it.isSelected && !queryParam.unSelected(it.value) }?.let { option ->
                params[filter.name] = option.value
            }
        }

        return params
    }
}