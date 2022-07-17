package com.listentoprabhupada.android.ui.screens

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.jetpack.Children
import com.arkivanov.decompose.extensions.compose.jetpack.animation.child.childAnimation
import com.arkivanov.decompose.extensions.compose.jetpack.animation.child.fade
import com.arkivanov.decompose.extensions.compose.jetpack.animation.child.plus
import com.arkivanov.decompose.extensions.compose.jetpack.animation.child.scale
import com.listentoprabhupada.android.ui.screens.favorites.FavoritesView
import com.listentoprabhupada.android.ui.screens.filters.FiltersView
import com.listentoprabhupada.android.ui.screens.downloads.DownloadsView
import com.listentoprabhupada.android.ui.screens.results.ResultsView
import com.listentoprabhupada.common.root.RootComponent

@Composable
fun MainContent(component: RootComponent) {
    Children(routerState = component.routerState, animation = childAnimation(fade() + scale())) {
        when (val child = it.instance) {
            is RootComponent.Child.Results -> ResultsView(child.component)
            is RootComponent.Child.Favorites -> FavoritesView(child.component)
            is RootComponent.Child.Downloads -> DownloadsView(child.component)
            is RootComponent.Child.Filters -> FiltersView(child.component)
        }
    }
}