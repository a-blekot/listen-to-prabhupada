package com.listentoprabhupada.android_ui.screens.root


import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import com.listentoprabhupada.android_ui.theme.Colors.navBarBg
import com.listentoprabhupada.android_ui.theme.Colors.navBarIcon
import com.listentoprabhupada.android_ui.theme.Colors.navBarIconSelected
import com.listentoprabhupada.android_ui.theme.Colors.navBarIconSelectedBg
import com.listentoprabhupada.android_ui.theme.Colors.navBarIconSelectedText
import com.listentoprabhupada.android_ui.theme.Colors.navBarText
import com.listentoprabhupada.common.root.RootComponent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.listentoprabhupada.android_ui.R
import com.listentoprabhupada.common.root.RootComponent.Child

@Composable
fun NavBar(activeComponent: Child, root: RootComponent, onSettings: () -> Unit) {
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
            selected = activeComponent is Child.Filters,
            onClick = root::onFiltersTabClicked,
            imageVector = Icons.Default.Tune,
            text = stringResource(R.string.nav_bar_filter),
        )

        NavBarItem(
            selected = activeComponent is Child.Settings,
            onClick = onSettings,
            imageVector = Icons.Outlined.Settings,
            text = stringResource(R.string.nav_bar_settings),
        )
    }
}

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

@Composable
private fun NavBarText(text: String) =
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall,
    )