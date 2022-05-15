package com.prabhupadalectures.common.root

import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.value.Value
import com.prabhupadalectures.common.filters.FiltersComponent
import com.prabhupadalectures.common.feature_results_api.ResultsComponent

interface RootComponent {

    val routerState: Value<RouterState<*, Child>>

    sealed interface Child {
        data class Results(val component: ResultsComponent) : Child
        data class Filters(val component: FiltersComponent) : Child
    }
}
