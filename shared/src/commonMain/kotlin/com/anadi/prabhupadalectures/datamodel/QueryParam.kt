package com.anadi.prabhupadalectures.datamodel

import com.anadi.prabhupadalectures.data.Database
import com.anadi.prabhupadalectures.data.filters.Filter
import com.anadi.prabhupadalectures.repository.FIRST_PAGE
import com.anadi.prabhupadalectures.repository.toDatabaseIdentifier
import com.anadi.prabhupadalectures.repository.toQueryParamsStringWithoutPage
import io.github.aakira.napier.Napier

const val PAGE_QUERY_KEY = "page"

data class QueryParam(
    val filterName: String,
    val selectedOption: String,
    val isSelected: Boolean
)

fun QueryParam?.unSelected(option: String) =
    this != null && !isSelected && selectedOption == option

fun buildQueryParams(
    queryParam: QueryParam?,
    filters: List<Filter>,
    currentPage: Int,
    db: Database
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