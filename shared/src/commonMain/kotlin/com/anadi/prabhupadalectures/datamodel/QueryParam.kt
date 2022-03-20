package com.anadi.prabhupadalectures.datamodel

import com.anadi.prabhupadalectures.data.filters.Filter

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
    currentPage: Int
): HashMap<String, Any> {

    val page =
        when {
            queryParam != null -> 1
            else -> currentPage.coerceAtLeast(1)
        }

    val params = HashMap<String, Any>().apply {
        put("page", page)

        if (queryParam?.isSelected == true) {
            put(queryParam.filterName, queryParam.selectedOption)
        }
    }

    filters.forEach { filter ->
        filter.options.firstOrNull { it.isSelected && !queryParam.unSelected(it.value) }?.let { option ->
            params[filter.name] = option.value
        }
    }

    return params
}