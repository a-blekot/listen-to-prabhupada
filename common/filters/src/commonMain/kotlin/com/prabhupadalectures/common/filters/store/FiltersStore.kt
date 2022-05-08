package com.prabhupadalectures.common.filters.store

import com.arkivanov.mvikotlin.core.store.Store
import com.prabhupadalectures.common.filters.data.Filter
import com.prabhupadalectures.common.filters.data.QueryParam
import com.prabhupadalectures.common.filters.store.FiltersStore.*

internal interface FiltersStore : Store<Intent, State, Label> {

    sealed interface Intent {
        object ClearAll : Intent
        object ApplyChanges : Intent
        data class UpdateFilter(val queryParam: QueryParam) : Intent
    }

    data class State(
        val isLoading: Boolean = false,
        val filters: List<Filter> = emptyList(),
        val totalLecturesCount: Int = 0,
        val pagesCount : Int = 0,
    )

    sealed interface Label {
        object ApplyChanges: Label
    }
}

