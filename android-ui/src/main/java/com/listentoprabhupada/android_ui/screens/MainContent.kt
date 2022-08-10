package com.listentoprabhupada.android_ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.listentoprabhupada.android_ui.R
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.*
import com.arkivanov.decompose.extensions.compose.jetpack.subscribeAsState
import com.listentoprabhupada.android_ui.screens.downloads.DownloadsView
import com.listentoprabhupada.android_ui.screens.favorites.FavoritesView
import com.listentoprabhupada.android_ui.screens.filters.FiltersView
import com.listentoprabhupada.android_ui.screens.results.ResultsView
import com.listentoprabhupada.common.root.RootComponent
import com.listentoprabhupada.common.root.RootComponent.Child

@Composable
fun MainContent(root: RootComponent, modifier: Modifier = Modifier) {
    val childStack by root.childStack.subscribeAsState()
    val activeComponent = childStack.active.instance

    Column(modifier = modifier) {

        Children(
            stack = root.childStack,
            modifier = Modifier.weight(weight = 1F),
            animation = stackAnimation(fade() + scale())
        ) {
            when (val child = it.instance) {
                is Child.Results -> ResultsView(child.component, modifier = Modifier.fillMaxSize())
                is Child.Favorites -> FavoritesView(child.component, modifier = Modifier.fillMaxSize())
                is Child.Downloads -> DownloadsView(child.component, modifier = Modifier.fillMaxSize())
                is Child.Filters -> FiltersView(child.component, modifier = Modifier.fillMaxSize())
                else -> throw IllegalArgumentException("No View for child: ${child.javaClass.simpleName}")
            }
        }

        NavigationBar(modifier = Modifier.fillMaxWidth()) {
            NavigationBarItem(
                selected = activeComponent is Child.Results,
                onClick = root::onResultsTabClicked,
                icon = {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = stringResource(R.string.nav_bar_results),
                    )
                },
                label = { NavBarText(text = stringResource(R.string.nav_bar_results)) },
            )

            NavigationBarItem(
                selected = activeComponent is Child.Favorites,
                onClick = root::onFavoritesTabClicked,
                icon = {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = stringResource(R.string.nav_bar_favorite),
                    )
                },
                label = { NavBarText(text = stringResource(R.string.nav_bar_favorite)) },
            )

            NavigationBarItem(
                selected = activeComponent is Child.Filters,
                onClick = root::onFiltersTabClicked,
                icon = {
                    Icon(
                        imageVector = Icons.Default.FilterAlt,
                        contentDescription = stringResource(R.string.nav_bar_filter),
                    )
                },
                label = { NavBarText(text = stringResource(R.string.nav_bar_filter)) },
            )

            NavigationBarItem(
                selected = activeComponent is Child.Downloads,
                onClick = root::onDownloadsTabClicked,
                icon = {
                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = "Загрузки",
                    )
                },
                label = { NavBarText(text = "Загрузки") },
            )

            NavigationBarItem(
                selected = activeComponent is Child.Settings,
                onClick = root::onSettingsTabClicked,
                icon = {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = stringResource(R.string.nav_bar_settings),
                    )
                },
                label = { NavBarText(text = stringResource(R.string.nav_bar_settings)) },
            )
        }
    }
}

@Composable
private fun NavBarText(text: String) =
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
        color = MaterialTheme.colorScheme.onPrimaryContainer,
        maxLines = 1,
    )


@OptIn(ExperimentalDecomposeApi::class)
@Composable
private fun tabAnimation(): StackAnimation<Any, Child> =
    stackAnimation { child, otherChild, direction ->
        val index = child.instance.index
        val otherIndex = otherChild.instance.index
        val anim = slide()
        if ((index > otherIndex) == direction.isEnter) anim else anim.flipSide()
    }

private val Child.index: Int
    get() =
        when (this) {
            is Child.Results -> 0
            is Child.Favorites -> 1
            is Child.Downloads -> 2
            is Child.Filters -> 3
            is Child.Settings -> 4
        }

@OptIn(ExperimentalDecomposeApi::class)
private fun StackAnimator.flipSide(): StackAnimator =
    StackAnimator { direction, onFinished, content ->
        invoke(
            direction = direction.flipSide(),
            onFinished = onFinished,
            content = content,
        )
    }

@Suppress("OPT_IN_USAGE")
private fun Direction.flipSide(): Direction =
    when (this) {
        Direction.ENTER_FRONT -> Direction.ENTER_BACK
        Direction.EXIT_FRONT -> Direction.EXIT_BACK
        Direction.ENTER_BACK -> Direction.ENTER_FRONT
        Direction.EXIT_BACK -> Direction.EXIT_FRONT
    }