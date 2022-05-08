package com.prabhupadalectures.common.filters

import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.prabhupadalectures.common.filters.data.Filter
import com.prabhupadalectures.common.filters.data.QueryParam

interface Filters {

    val models: Value<Model>

    fun onClearAll() {}
    fun onQueryParam(queryParam: QueryParam) {}
    fun onApplyChanges() {}
    fun onFilterExpanded(filterName: String, isExpanded: Boolean) {}

    data class Model(
        val isLoading: Boolean,
        val filters: List<Filter>,
        val totalLecturesCount: Int,
        val pagesCount : Int,
    )

    sealed class Output {
        object ShowResults : Output()
    }
}