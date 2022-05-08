package com.prabhupadalectures.android.ui.screens

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.jetpack.Children
import com.arkivanov.decompose.extensions.compose.jetpack.animation.child.childAnimation
import com.arkivanov.decompose.extensions.compose.jetpack.animation.child.fade
import com.arkivanov.decompose.extensions.compose.jetpack.animation.child.plus
import com.arkivanov.decompose.extensions.compose.jetpack.animation.child.scale
import com.prabhupadalectures.android.ui.screens.filters.FiltersView
import com.prabhupadalectures.android.ui.screens.results.ResultsView
import com.prabhupadalectures.common.root.Root

@Composable
fun MainContent(component: Root) {
    Children(routerState = component.routerState, animation = childAnimation(fade() + scale())) {
        when (val child = it.instance) {
            is Root.Child.ChildResults -> ResultsView(child.component)
            is Root.Child.ChildFilters -> FiltersView(child.component)
        }
    }
}