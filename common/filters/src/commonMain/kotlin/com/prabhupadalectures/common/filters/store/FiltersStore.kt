package com.prabhupadalectures.common.filters.store

import com.arkivanov.mvikotlin.core.store.Store
import com.prabhupadalectures.common.filters.FiltersState
import com.prabhupadalectures.common.filters.data.QueryParam
import com.prabhupadalectures.common.filters.store.FiltersStore.*

internal interface FiltersStore : Store<Intent, FiltersState, Label> {

    sealed interface Intent {
        object ClearAll : Intent
        object ApplyChanges : Intent
        data class UpdateFilter(val queryParam: QueryParam) : Intent
    }

    sealed interface Label {
        object ApplyChanges: Label
    }
}

