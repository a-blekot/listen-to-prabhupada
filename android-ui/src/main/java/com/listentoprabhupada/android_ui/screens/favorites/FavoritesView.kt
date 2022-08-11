package com.listentoprabhupada.android_ui.screens.favorites

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetpack.subscribeAsState
import com.listentoprabhupada.android_ui.custom.StandartLazyColumn
import com.listentoprabhupada.android_ui.helpers.LectureListItem
import com.listentoprabhupada.android_ui.theme.Dimens.paddingM
import com.listentoprabhupada.common.favorites_api.FavoritesComponent

@Composable
fun FavoritesView(component: FavoritesComponent, modifier: Modifier = Modifier) {
    val state = component.flow.subscribeAsState()

    Box {
        StandartLazyColumn(modifier = modifier, itemPadding = paddingM) {
            items(state.value.lectures, key = { it.id }) { lectureItem ->
                LectureListItem(lectureItem, component )
            }
        }
    }
}
