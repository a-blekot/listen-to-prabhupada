package com.listentoprabhupada.android_ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.listentoprabhupada.android_ui.R
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.*
import com.arkivanov.decompose.extensions.compose.jetpack.subscribeAsState
import com.listentoprabhupada.android_ui.screens.downloads.DownloadsView
import com.listentoprabhupada.android_ui.screens.favorites.FavoritesView
import com.listentoprabhupada.android_ui.screens.filters.FiltersView
import com.listentoprabhupada.android_ui.screens.results.ResultsView
import com.listentoprabhupada.android_ui.theme.Colors.background
import com.listentoprabhupada.android_ui.theme.Colors.navBarBg
import com.listentoprabhupada.android_ui.theme.Colors.navBarIcon
import com.listentoprabhupada.android_ui.theme.Colors.navBarIconSelected
import com.listentoprabhupada.android_ui.theme.Colors.navBarIconSelectedBg
import com.listentoprabhupada.android_ui.theme.Colors.navBarIconSelectedText
import com.listentoprabhupada.android_ui.theme.Colors.navBarText
import com.listentoprabhupada.common.root.RootComponent
import com.listentoprabhupada.common.root.RootComponent.Child

@Composable
fun RootContent(root: RootComponent, modifier: Modifier = Modifier) {
    val childStack by root.childStack.subscribeAsState()
    val activeComponent = childStack.active.instance

    Column(modifier = modifier.background(background())) {
        Children(
            stack = root.childStack,
            modifier = Modifier.weight(weight = 1F),
            animation = tabAnimation()
        ) {
            when (val child = it.instance) {
                is Child.Results -> ResultsView(child.component, modifier = Modifier.fillMaxSize())
                is Child.Favorites -> FavoritesView(child.component, modifier = Modifier.fillMaxSize())
                is Child.Downloads -> DownloadsView(child.component, modifier = Modifier.fillMaxSize())
                is Child.Filters -> FiltersView(child.component, modifier = Modifier.fillMaxSize())
                else -> throw IllegalArgumentException("No View for child: ${child.javaClass.simpleName}")
            }
        }

        NavigationBar(
            modifier = Modifier.fillMaxWidth(),
            containerColor = navBarBg(),
            contentColor = navBarIcon(),
        ) {
            NavBarItem(
                selected = activeComponent is Child.Results,
                onClick = root::onResultsTabClicked,
                imageVector = Icons.Default.Hearing,
                text = stringResource(R.string.nav_bar_results),
            )

            NavBarItem(
                selected = activeComponent is Child.Favorites,
                onClick = root::onFavoritesTabClicked,
                imageVector = Icons.Default.FavoriteBorder,
                text = stringResource(R.string.nav_bar_favorite),
            )

            NavBarItem(
                selected = activeComponent is Child.Filters,
                onClick = root::onFiltersTabClicked,
                imageVector = Icons.Default.Tune,
                text = stringResource(R.string.nav_bar_filter),
            )

            NavBarItem(
                selected = activeComponent is Child.Downloads,
                onClick = root::onDownloadsTabClicked,
                imageVector = Icons.Default.Download,
                text = "Загрузки",
            )

            NavBarItem(
                selected = activeComponent is Child.Settings,
                onClick = root::onSettingsTabClicked,
                imageVector = Icons.Outlined.Settings,
                text = stringResource(R.string.nav_bar_settings),
            )
        }
    }
}

@Composable
private fun NavBarText(text: String) =
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall,
    )

@Composable
private fun RowScope.NavBarItem(
    selected: Boolean,
    onClick: () -> Unit,
    imageVector: ImageVector,
    text: String
) =
    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        icon = { Icon(imageVector, text) },
        label = { NavBarText(text) },
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = navBarIconSelected(),
            unselectedIconColor = navBarIcon(),
            selectedTextColor = navBarIconSelectedText(),
            unselectedTextColor = navBarText(),
            indicatorColor = navBarIconSelectedBg(),
        )
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