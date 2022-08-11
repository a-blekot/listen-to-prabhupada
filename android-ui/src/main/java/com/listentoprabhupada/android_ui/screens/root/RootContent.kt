package com.listentoprabhupada.android_ui.screens.root

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import com.arkivanov.decompose.extensions.compose.jetpack.subscribeAsState
import com.listentoprabhupada.android_ui.R
import com.listentoprabhupada.android_ui.custom.StandartColumn
import com.listentoprabhupada.android_ui.custom.StandartRow
import com.listentoprabhupada.android_ui.helpers.PlayerListItem
import com.listentoprabhupada.android_ui.screens.downloads.DownloadsView
import com.listentoprabhupada.android_ui.screens.favorites.FavoritesView
import com.listentoprabhupada.android_ui.screens.filters.FiltersView
import com.listentoprabhupada.android_ui.screens.results.ResultsView
import com.listentoprabhupada.android_ui.theme.Colors.background
import com.listentoprabhupada.android_ui.theme.Colors.surface
import com.listentoprabhupada.android_ui.theme.Dimens.bottomSheetPeekHeight
import com.listentoprabhupada.android_ui.theme.Dimens.horizontalScreenPadding
import com.listentoprabhupada.android_ui.theme.Dimens.iconSizeL
import com.listentoprabhupada.android_ui.theme.Dimens.iconSizeM
import com.listentoprabhupada.android_ui.theme.Dimens.paddingL
import com.listentoprabhupada.android_ui.theme.Dimens.paddingM
import com.listentoprabhupada.common.root.RootComponent
import com.listentoprabhupada.common.root.RootComponent.Child
import kotlinx.coroutines.launch

@OptIn(ExperimentalDecomposeApi::class, ExperimentalMaterialApi::class)
@Composable
fun RootContent(root: RootComponent, modifier: Modifier = Modifier) {
    val childStack by root.childStack.subscribeAsState()
    val activeComponent = childStack.active.instance

    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()

//    scope.launch {
//        scaffoldState.snackbarHostState.showSnackbar("Snackbar #${++clickCount}")
//    }

    Column(modifier = modifier.background(background())) {
        BottomSheetScaffold(
            scaffoldState = scaffoldState,
            modifier = Modifier.weight(weight = 1F),
            sheetContent = {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                ) {
                    PlayerListItem(root.playerComponent, modifier)
                }
            },
            sheetPeekHeight = bottomSheetPeekHeight,
            drawerContent = {
                DrawerContent(root)
            },
        ) {
            Children(
                stack = root.childStack,
                modifier = Modifier.fillMaxSize(),
                animation = tabAnimation()
            ) {

                val modifier = modifier.fillMaxSize().padding(bottom = bottomSheetPeekHeight)
                
                when (val child = it.instance) {
                    is Child.Results -> ResultsView(child.component, modifier)
                    is Child.Favorites -> FavoritesView(child.component, modifier)
                    is Child.Downloads -> DownloadsView(child.component, modifier)
                    is Child.Filters -> FiltersView(child.component, modifier)
                    else -> throw IllegalArgumentException("No View for child: ${child.javaClass.simpleName}")
                }
            }
        }

        NavBar(activeComponent, root) {
            scope.launch { scaffoldState.toggleDrawer() }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
private suspend fun BottomSheetScaffoldState.toggleDrawer() {
    if (drawerState.isClosed) {
        drawerState.open()
    } else if (drawerState.isOpen) {
        drawerState.close()
    }
}

@Composable
fun DrawerContent(root: RootComponent) {
    StandartColumn(
        modifier = Modifier
            .background(surface())
            .padding(all = paddingL)
    ) {
        StandartRow(
            modifier = Modifier.clickable { root.onFavoritesTabClicked() },
            horizontalArrangement = Arrangement.spacedBy(paddingM)
        ) {
            val text = stringResource(R.string.nav_bar_favorite)
            Icon(
                Icons.Default.Favorite,
                text,
                modifier = Modifier.size(iconSizeM),
                tint = MaterialTheme.colorScheme.tertiary
            )
            Text(
                text = text,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.tertiary
            )
        }

        StandartRow(
            modifier = Modifier.clickable { root.onDownloadsTabClicked() },
            horizontalArrangement = Arrangement.spacedBy(paddingM)
        ) {
            val text = stringResource(R.string.nav_bar_downloads)
            Icon(
                Icons.Default.Download,
                text,
                modifier = Modifier.size(iconSizeM),
                tint = MaterialTheme.colorScheme.tertiary
            )
            Text(
                text = text,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.tertiary
            )
        }
    }
}