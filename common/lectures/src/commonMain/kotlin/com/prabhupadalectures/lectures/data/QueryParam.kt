package com.prabhupadalectures.lectures.data

import com.prabhupadalectures.common.database.Database
import com.prabhupadalectures.common.network_api.PAGE_QUERY_KEY
import com.prabhupadalectures.lectures.data.filters.Filter
import com.prabhupadalectures.lectures.repository.FIRST_PAGE
import com.prabhupadalectures.lectures.repository.toDatabaseIdentifier
import io.github.aakira.napier.Napier

data class QueryParam(
    val filterName: String = "",
    val selectedOption: String = "",
    val isSelected: Boolean = false
)

fun QueryParam?.unSelected(option: String) =
    this != null && !isSelected && selectedOption == option

fun buildQueryParams(
    db: Database,
    filters: List<Filter> = emptyList(),
    currentPage: Int = FIRST_PAGE,
    queryParam: QueryParam? = null
): HashMap<String, Any> {

    val params = HashMap<String, Any>()

    filters.sortedBy { it.name }.forEach { filter ->
        filter.options.firstOrNull { it.isSelected && !queryParam.unSelected(it.value) }?.let { option ->
            params[filter.name] = option.value
        }
    }

    if (queryParam?.isSelected == true) {
        params[queryParam.filterName] = queryParam.selectedOption
    }

    val page =
        when {
            queryParam != null -> {
                val id = params.toDatabaseIdentifier()
                val page = db.selectPage(id)
                Napier.d("load: id=$id, page=$page", tag = "PAGE_DB")
                page
//                db.selectPage(params.toDatabaseIdentifier())
            }
            else -> currentPage.coerceAtLeast(FIRST_PAGE)
        }

    params[PAGE_QUERY_KEY] = page

    return params
}