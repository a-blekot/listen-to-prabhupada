package com.listentoprabhupada.android_ui.screens.root

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.StackAnimation
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.stackAnimation
import com.listentoprabhupada.common.root.RootComponent


@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun tabAnimation(): StackAnimation<Any, RootComponent.Child> =
    stackAnimation { child, otherChild, direction ->
//        val index = child.instance.index
//        val otherIndex = otherChild.instance.index
        slide()
//        val anim = slide()
//        if ((index > otherIndex) == direction.isEnter) anim else anim.flipSide()
    }

private val RootComponent.Child.index: Int
    get() =
        when (this) {
            is RootComponent.Child.Results -> 0
            is RootComponent.Child.Favorites -> 1
            is RootComponent.Child.Downloads -> 2
            is RootComponent.Child.Filters -> 3
            is RootComponent.Child.Settings -> 4
            is RootComponent.Child.Donations -> 5
        }

//@OptIn(ExperimentalDecomposeApi::class)
//private fun StackAnimator.flipSide(): StackAnimator =
//    StackAnimator { direction, onFinished, content ->
//        invoke(
//            direction = direction.flipSide(),
//            onFinished = onFinished,
//            content = content,
//        )
//    }

//@Suppress("OPT_IN_USAGE")
//private fun com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.Direction.flipSide(): Direction =
//    when (this) {
//        Direction.ENTER_FRONT -> Direction.ENTER_BACK
//        Direction.EXIT_FRONT -> Direction.EXIT_BACK
//        Direction.ENTER_BACK -> Direction.ENTER_FRONT
//        Direction.EXIT_BACK -> Direction.EXIT_FRONT
//    }