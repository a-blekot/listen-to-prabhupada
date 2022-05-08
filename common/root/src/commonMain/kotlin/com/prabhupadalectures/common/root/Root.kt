package com.prabhupadalectures.common.root

import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.value.Value
import com.prabhupadalectures.common.filters.Filters
import com.prabhupadalectures.lectures.mvi.lectures.Results

interface Root {

    val routerState: Value<RouterState<*, Child>>

    sealed interface Child {
        data class ChildResults(val component: Results) : Child
        data class ChildFilters(val component: Filters) : Child
    }
}
