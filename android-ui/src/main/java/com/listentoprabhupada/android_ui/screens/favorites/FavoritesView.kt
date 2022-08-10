package com.listentoprabhupada.android_ui.screens.favorites

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetpack.subscribeAsState
import com.listentoprabhupada.android_ui.custom.StandartLazyColumn
import com.listentoprabhupada.android_ui.helpers.LectureListItem
import com.listentoprabhupada.android_ui.helpers.PlayerListItem
import com.listentoprabhupada.android_ui.theme.Dimens.paddingM
import com.listentoprabhupada.common.feature_favorites_api.FavoritesFeatureComponent

@Composable
fun FavoritesView(component: FavoritesFeatureComponent, modifier: Modifier = Modifier) =
    component.run {
    val favoritesState = favoritesComponent.flow.subscribeAsState()

    Box {
        StandartLazyColumn(itemPadding = paddingM) {
            items(favoritesState.value.lectures, key = { it.id }) { lectureItem ->
                LectureListItem(lectureItem, favoritesComponent )
            }

            item { PlayerListItem(playerComponent, modifier) }
        }
    }
}
