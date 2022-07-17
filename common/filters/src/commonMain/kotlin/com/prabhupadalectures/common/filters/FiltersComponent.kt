package com.listentoprabhupada.common.filters

import com.arkivanov.decompose.value.Value
import com.listentoprabhupada.common.filters.data.Filter
import com.listentoprabhupada.common.filters.data.QueryParam

interface FiltersComponent {

    val models: Value<FiltersState>

    fun onClearAll() {}
    fun onQueryParam(queryParam: QueryParam) {}
    fun onApplyChanges() {}
    fun onFilterExpanded(filterName: String, isExpanded: Boolean) {}

    sealed class Output {
        object ShowResults : Output()
    }
}