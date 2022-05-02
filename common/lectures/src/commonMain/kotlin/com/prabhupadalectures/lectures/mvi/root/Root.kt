package com.prabhupadalectures.lectures.mvi.root

import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.value.Value
import com.prabhupadalectures.lectures.mvi.lectures.Results

interface Root {

    val routerState: Value<RouterState<*, Child>>

    sealed class Child {
        data class ResultsChild(val component: Results) : Child()
    }
}