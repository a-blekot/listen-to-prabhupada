package com.anadi.prabhupadalectures.android.viewmodel

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.getViewModel
import io.github.aakira.napier.Napier

@Suppress("MoveLambdaOutsideParentheses")
@Composable
inline fun <reified VM : BaseViewModel<*, *, *>> Screen.WithViewModel(
    crossinline onEffect: suspend (UiEffect) -> Unit,
    initialize: (VM) -> Unit = {},
    start: (VM, (UiEvent) -> Unit) -> Unit,
) {
    val viewModel: VM = getViewModel()

    viewModel.collectEffect { effect ->
        Napier.e("WithViewModel - onEffectOuter")
        onEffect(effect)
    }

    initialize(viewModel)

    val onEvent: (UiEvent) -> Unit = { event -> viewModel.setEvent(event) }

    start(
        viewModel,
        onEvent,
    )
}