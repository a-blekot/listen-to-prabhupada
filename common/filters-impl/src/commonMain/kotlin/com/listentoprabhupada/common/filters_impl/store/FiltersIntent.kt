package com.listentoprabhupada.common.filters_impl.store

import com.listentoprabhupada.common.filters_api.QueryParam

sealed interface FiltersIntent {
    object ClearAll : FiltersIntent
    object ApplyChanges : FiltersIntent
    data class UpdateFilter(val queryParam: QueryParam) : FiltersIntent
}