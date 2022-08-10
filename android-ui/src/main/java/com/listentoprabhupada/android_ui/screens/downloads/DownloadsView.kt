package com.listentoprabhupada.android_ui.screens.downloads

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetpack.subscribeAsState
import com.listentoprabhupada.android_ui.custom.StandartLazyColumn
import com.listentoprabhupada.android_ui.helpers.LectureListItem
import com.listentoprabhupada.android_ui.helpers.PlayerListItem
import com.listentoprabhupada.android_ui.theme.Dimens.paddingM
import com.listentoprabhupada.common.feature_downloads_api.DownloadsFeatureComponent

@Composable
fun DownloadsView(component: DownloadsFeatureComponent, modifier: Modifier = Modifier) =
    component.run {
    val downloadsState = downloadsComponent.flow.subscribeAsState()

    Box {
        StandartLazyColumn(itemPadding = paddingM) {
            items(downloadsState.value.lectures, key = { it.id }) { lectureItem ->
                LectureListItem(lectureItem, downloadsComponent)
            }

            item { PlayerListItem(playerComponent, modifier) }
        }
    }
}
