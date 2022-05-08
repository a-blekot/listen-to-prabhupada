package com.prabhupadalectures.common.filters.data

import com.prabhupadalectures.common.network_api.FIRST_PAGE
import com.prabhupadalectures.common.network_api.PAGE_QUERY_KEY

data class QueryParam(
    val filterName: String = "",
    val selectedOption: String = "",
    val isSelected: Boolean = false
)

fun QueryParam?.unSelected(option: String) =
    this != null && !isSelected && selectedOption == option

fun buildQueryParams(
    filters: List<Filter> = emptyList(),
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

    params[PAGE_QUERY_KEY] = FIRST_PAGE

    return params
}