package com.listentoprabhupada.common.filters_impl.data

import com.listentoprabhupada.common.filters_api.Filter
import com.listentoprabhupada.common.filters_api.QueryParam
import com.listentoprabhupada.common.network_api.FIRST_PAGE
import com.listentoprabhupada.common.network_api.PAGE_QUERY_KEY

fun QueryParam?.unSelected(filterName: String, optionValue: String) =
    this != null && !isSelected && this.filterName == filterName && selectedOption == optionValue

fun buildQueryParams(
    filters: List<Filter> = emptyList(),
    queryParam: QueryParam? = null
): HashMap<String, Any> {

    val params = HashMap<String, Any>()

    filters.sortedBy { it.name }.forEach { filter ->
        filter.options.firstOrNull { option ->
            option.isSelected && !queryParam.unSelected(filter.name, option.value)
        }?.let { option ->
            params[filter.name] = option.value
        }
    }

    if (queryParam?.isSelected == true) {
        params[queryParam.filterName] = queryParam.selectedOption
    }

    params[PAGE_QUERY_KEY] = FIRST_PAGE

    return params
}