package com.anadi.prabhupadalectures.repository

import com.anadi.prabhupadalectures.data.Database
import com.anadi.prabhupadalectures.data.filters.Filter
import com.anadi.prabhupadalectures.data.lectures.Lecture
import com.anadi.prabhupadalectures.datamodel.ApiMapper
import com.anadi.prabhupadalectures.datamodel.Pagination
import com.anadi.prabhupadalectures.datamodel.Playlist
import com.anadi.prabhupadalectures.datamodel.QueryParam
import com.anadi.prabhupadalectures.network.api.ApiModel
import com.anadi.prabhupadalectures.network.api.PrabhupadaApi
import com.anadi.prabhupadalectures.network.api.QueryParams
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class State(
    val loading: Boolean,
    val filters: List<Filter> = emptyList(),
    val playlist: Playlist = Playlist()
)

data class PlaybackState(
    val lectureId: Long = 0L,
    val title: String = "",
    val description: String = "",
    val isPlaying: Boolean = false,
    val hasNext: Boolean = false,
    val hasPrevious: Boolean = false,
    val timeMs: Long = 0L,
    val durationMs: Long = 1L
)

class RepositoryImpl(
    private val db: Database,
    private val api: PrabhupadaApi,
    withLog: Boolean
) :
    CoroutineScope by CoroutineScope(Dispatchers.Main), Repository {

    private val state = MutableStateFlow(State(false))
    private val playbackState = MutableStateFlow(PlaybackState())
    private var pagination = Pagination()

    init {
        if (withLog) Napier.base(DebugAntilog())
    }

    override fun observeState(): StateFlow<State> = state
    override fun observePlaybackState(): StateFlow<PlaybackState> = playbackState

    override fun setPlaybackState(newState: PlaybackState) {
        playbackState.value = newState
    }

    private fun printFavorites() =
        db.getAllFavorites().forEach {
            Napier.d("favorite $it}")
        }

    override fun addFavorite(id: Long) {
        db.setFavorite(id)
        refreshFavorites()
        printFavorites()
    }

    override fun removeFavorite(id: Long) {
        db.removeFavorite(id)
        refreshFavorites()
        printFavorites()
    }

    override fun savePosition(lectureId: Long, timeMs: Long) =
        runBlocking { db.savePosition(lectureId, timeMs) }

    override fun getSavedPosition(lectureId: Long) =
        runBlocking { db.getSavedPosition(lectureId) }

    override suspend fun updateQuery(queryParam: QueryParam) =
        loadMore(queryParam)

    override suspend fun init() =
        loadMore(settings.getFilters())

    override suspend fun loadMore(queryParam: QueryParam?) =
        loadMore(buildQueryParams(queryParam))

    override suspend fun loadMore(queryParams: QueryParams) = withContext(Dispatchers.Default) {
        setLoading(true)

        Napier.d("firstLoad: pagination = $pagination")
        val result = api.getResults(queryParams)
        if (result.isSuccess) {
            result.getOrNull()?.let {
                updatePagination(it)
                updateData(it)
            }
        } else {
            Napier.e(message = "api.getResults isFailure", tag = "DataModel", throwable = result.exceptionOrNull())
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
        val page =
            when {
                queryParam != null -> 1
                else -> pagination.curr.coerceAtLeast(1)
            }

        val params = HashMap<String, Any>().apply {
            put("page", page)

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