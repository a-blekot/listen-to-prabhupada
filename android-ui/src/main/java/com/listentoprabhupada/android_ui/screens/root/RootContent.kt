package com.listentoprabhupada.android_ui.screens.root

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.listentoprabhupada.android_ui.theme.Dimens
import com.listentoprabhupada.android_ui.theme.Dimens.bottomSheetHeight
import com.listentoprabhupada.android_ui.theme.Dimens.bottomSheetPeekHeight
import com.listentoprabhupada.android_ui.theme.Dimens.horizontalScreenPadding
import com.listentoprabhupada.android_ui.theme.Dimens.iconSizeL
import com.listentoprabhupada.android_ui.theme.Dimens.iconSizeM
import com.listentoprabhupada.android_ui.theme.Dimens.paddingL
import com.listentoprabhupada.android_ui.theme.Dimens.paddingM
import com.listentoprabhupada.android_ui.theme.Dimens.paddingS
import com.listentoprabhupada.android_ui.theme.Dimens.radiusXL
import com.listentoprabhupada.common.root.RootComponent
import com.listentoprabhupada.common.root.RootComponent.Child
import com.listentoprabhupada.common.settings_api.SettingsComponent
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
                PlayerListItem(root.playerComponent, Modifier.fillMaxWidth().height(bottomSheetHeight))
            },
            sheetShape = RoundedCornerShape(topStart = radiusXL, topEnd = radiusXL),
            sheetBackgroundColor = Color.Transparent,
            sheetPeekHeight = bottomSheetPeekHeight,
            drawerContent = {
                DrawerContent(root, root.settingsComponent)
            },
        ) {
            Children(
                stack = root.childStack,
                modifier = Modifier.fillMaxSize(),
                animation = tabAnimation()
            ) {

                when (val child = it.instance) {
                    is Child.Results -> ResultsView(child.component, childModifier())
                    is Child.Favorites -> FavoritesView(child.component, childModifier())
                    is Child.Downloads -> DownloadsView(child.component, childModifier())
                    is Child.Filters -> FiltersView(child.component, childModifier())
                    is Child.Donations -> TODO("create donations screen")
                    else -> throw IllegalArgumentException("No View for child: ${child.javaClass.simpleName}")
                }
            }
        }

        NavBar(activeComponent, root) {
            scope.launch { scaffoldState.toggleDrawer() }
        }
    }
}

@Composable
private fun childModifier() =
    Modifier.fillMaxSize().padding(bottom = bottomSheetPeekHeight)

@OptIn(ExperimentalMaterialApi::class)
private suspend fun BottomSheetScaffoldState.toggleDrawer() {
    if (drawerState.isClosed) {
        drawerState.open()
    } else if (drawerState.isOpen) {
        drawerState.close()
    }
}

@Composable
fun DrawerContent(root: RootComponent, settings: SettingsComponent) {
    val state = settings.flow.subscribeAsState()

    StandartColumn(
        modifier = Modifier
            .background(surface())
            .padding(vertical = paddingL)
            .padding(start = paddingS)
    ) {
        StandartRow(
            modifier = Modifier.clickable { root.onFavoritesTabClicked() },
            horizontalArrangement = Arrangement.spacedBy(paddingM)
        ) {
            val text = stringResource(R.string.nav_bar_favorite)
            Icon(
                Icons.Default.Favorite,
                text,
                modifier = Modifier.size(iconSizeL),
                tint = colorScheme.tertiary
            )
            Text(
                text = text,
                style = typography.titleMedium,
                color = colorScheme.tertiary
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
                modifier = Modifier.size(iconSizeL),
                tint = colorScheme.tertiary
            )
            Text(
                text = text,
                style = typography.titleMedium,
                color = colorScheme.tertiary
            )
        }

        StandartRow(
            horizontalArrangement = Arrangement.spacedBy(paddingM),
            modifier = Modifier.clickable { settings.donations() }.padding(top = paddingL)
        ) {
            Icon(
                Icons.Rounded.VolunteerActivism,
                "donations",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(Dimens.iconSizeL)
            )

            Text(
                text = stringResource(R.string.label_donate), // todo moko
                style = typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1,
            )
        }

        StandartRow(
            horizontalArrangement = Arrangement.spacedBy(paddingM),
            modifier = Modifier.clickable { settings.sendEmail() }
        ) {

            Icon(
                Icons.Rounded.Email,
                "Email",
                tint = colorScheme.tertiary,
                modifier = Modifier.size(iconSizeL)
            )

            Text(
                text = stringResource(R.string.label_feedback), // todo moko
                style = typography.titleMedium,
                color = colorScheme.tertiary,
                maxLines = 1,
            )
        }

        StandartRow(
            horizontalArrangement = Arrangement.spacedBy(paddingM),
            modifier = Modifier.clickable { settings.shareApp() }
        ) {

            Icon(
                Icons.Rounded.Share,
                "Share",
                tint = colorScheme.tertiary,
                modifier = Modifier.size(iconSizeL)
            )

            Text(
                text = stringResource(R.string.label_share_app), // todo moko
                style = typography.titleMedium,
                color = colorScheme.tertiary,
                maxLines = 1,
            )
        }

        StandartRow(
            horizontalArrangement = Arrangement.spacedBy(paddingM),
            modifier = Modifier.clickable { settings.rateUs() }
        ) {

            Icon(
                Icons.Rounded.Star,
                "Rate us",
                tint = colorScheme.tertiary,
                modifier = Modifier.size(iconSizeL)
            )

            Text(
                text = stringResource(R.string.label_rate_us), // todo moko
                style = typography.titleMedium,
                color = colorScheme.tertiary,
                maxLines = 1,
            )
        }

        Divider(
            color = colorScheme.tertiary,
            thickness = Dimens.borderS
        )

        Text(
            text = "${stringResource(R.string.label_version)} ${state.value.versionInfo.versionName}", // todo moko
            style = typography.titleMedium,
            color = colorScheme.tertiary,
            maxLines = 1,
        )
    }
}
