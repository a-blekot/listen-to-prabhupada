package com.listentoprabhupada.android_ui.screens.downloads

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetpack.subscribeAsState
import com.listentoprabhupada.android_ui.helpers.LectureListItem
import com.listentoprabhupada.android_ui.helpers.Listener
import com.listentoprabhupada.android_ui.helpers.PlayerListItem
import com.listentoprabhupada.common.feature_downloads_api.DownloadsFeatureComponent

@Composable
fun DownloadsView(component: DownloadsFeatureComponent) {
    val downloadsState = component.downloadsComponent.flow.subscribeAsState()

    Box {
        LazyColumn(
            Modifier.fillMaxWidth().padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(downloadsState.value.lectures, key = { it.id }) { lectureItem ->
                LectureListItem(
                    lecture = lectureItem,
                    component = object: Listener {
                        override fun onPause() = component.downloadsComponent.onPause()

                        override fun onPlay(id: Long) = component.downloadsComponent.onPlay(id)

                        override fun onFavorite(id: Long, isFavorite: Boolean) =
                            component.downloadsComponent.onFavorite(id, isFavorite)
                    },
                )
            }

            item {
                PlayerListItem(playerComponent = component.playerComponent)
            }
        }
    }
}
