package com.listentoprabhupada.common.filters_api

import com.arkivanov.decompose.value.Value

interface FiltersComponent {

    val models: Value<FiltersState>

    fun onClearAll() {}
    fun search(text: String) {}
    fun onQueryParam(queryParam: QueryParam) {}
    fun onApplyChanges() {}
    fun onFilterExpanded(filterName: String, isExpanded: Boolean) {}
}