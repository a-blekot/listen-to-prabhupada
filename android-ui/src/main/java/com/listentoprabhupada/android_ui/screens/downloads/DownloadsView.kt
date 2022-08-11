package com.listentoprabhupada.android_ui.screens.downloads

import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetpack.subscribeAsState
import com.listentoprabhupada.android_ui.custom.StandartLazyColumn
import com.listentoprabhupada.android_ui.helpers.LectureListItem
import com.listentoprabhupada.android_ui.theme.Dimens.paddingM
import com.listentoprabhupada.common.downloads_api.DownloadsComponent

@Composable
fun DownloadsView(component: DownloadsComponent, modifier: Modifier = Modifier) {
    val downloadsState = component.flow.subscribeAsState()

    StandartLazyColumn(modifier = modifier, itemPadding = paddingM) {
        items(downloadsState.value.lectures, key = { it.id }) { lectureItem ->
            LectureListItem(lectureItem, component)
        }
    }
}
