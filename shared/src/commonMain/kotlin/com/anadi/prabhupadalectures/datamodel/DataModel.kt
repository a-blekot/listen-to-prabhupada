package com.anadi.prabhupadalectures.datamodel

import com.anadi.prabhupadalectures.data.filters.Filter
import com.anadi.prabhupadalectures.data.lectures.Lecture
import com.anadi.prabhupadalectures.network.api.ApiModel
import com.anadi.prabhupadalectures.network.api.QueryParams
import com.anadi.prabhupadalectures.repository.Repository
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter

data class State(
    val loading: Boolean,
    val lectures: List<Lecture> = emptyList(),
    val filters: List<Filter> = emptyList()
)

class DataModel(private val repository: Repository, withLog: Boolean) :
    CoroutineScope by CoroutineScope(Dispatchers.Main) {

    private val state = MutableStateFlow(State(false))
    private var pagination = Pagination()

    init {
        if (withLog) Napier.base(DebugAntilog())
    }

    fun observeState(): StateFlow<State> = state

    suspend fun updateQuery(queryParam: QueryParam) =
        loadMore(queryParam)

    suspend fun loadMore(queryParam: QueryParam? = null) {
        setLoading(true)

        Napier.d("firstLoad: pagination = $pagination")
        val result = repository.getResults(buildQueryParams(queryParam))
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

    private fun setLoading(loading: Boolean) =
        pushNewState(state.value.copy(loading = loading))

    private fun updateData(apiModel: ApiModel) {
        val state = State(
            loading = false,
            lectures = ApiMapper.lectures(apiModel),
            filters = ApiMapper.filters(apiModel)
        )

        pushNewState(state)
    }

    private fun pushNewState(newState: State) {
        if (newState != state.value) {
//            Napier.d(tag = "DataModel", message = "NewState: $newState")
            state.value = newState
        }
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

//    data class QueryParams(
//        val page: Int,
//        val category: Int? = null,
//        val fileType: Int? = null,
//        val year: Int? = null,
//        val month: Int? = null,
//        val scripture: Int? = null,
//        val canto: Int? = null,
//        val chapter: Int? = null,
//        val verse: Int? = null,
//        val country: Int? = null,
//        val city: Int? = null
//    )
}