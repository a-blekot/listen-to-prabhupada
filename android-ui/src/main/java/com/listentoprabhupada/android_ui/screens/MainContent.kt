package com.listentoprabhupada.android_ui.screens

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.stackAnimation
import com.listentoprabhupada.android_ui.screens.favorites.FavoritesView
import com.listentoprabhupada.android_ui.screens.filters.FiltersView
import com.listentoprabhupada.android_ui.screens.downloads.DownloadsView
import com.listentoprabhupada.android_ui.screens.results.ResultsView
import com.listentoprabhupada.common.root.RootComponent

@Composable
fun MainContent(component: RootComponent) {
    Children(stack = component.childStack, animation = stackAnimation(fade() + scale())) {
        when (val child = it.instance) {
            is RootComponent.Child.Results -> ResultsView(child.component)
            is RootComponent.Child.Favorites -> FavoritesView(child.component)
            is RootComponent.Child.Downloads -> DownloadsView(child.component)
            is RootComponent.Child.Filters -> FiltersView(child.component)
            else -> throw IllegalArgumentException("No View for child: ${child.javaClass.simpleName}")
        }
    }
}