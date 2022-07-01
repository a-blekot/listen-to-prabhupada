package com.prabhupadalectures.android.ui.screens.favorites

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetpack.subscribeAsState
import com.prabhupadalectures.android.ui.screens.helpers.*
import com.prabhupadalectures.common.feature_favorites_api.FavoritesFeatureComponent

@Composable
fun FavoritesView(component: FavoritesFeatureComponent) {
    val favoritesState = component.favoritesComponent.flow.subscribeAsState()

    Box {
        LazyColumn(
            Modifier.fillMaxWidth()
        ) {
            items(favoritesState.value.lectures, key = { it.id }) { lectureItem ->
                LectureListItem(
                    lecture = lectureItem,
                    component = object: Listener {
                        override fun onPause() = component.favoritesComponent.onPause()

                        override fun onPlay(id: Long) = component.favoritesComponent.onPlay(id)

                        override fun onFavorite(id: Long, isFavorite: Boolean) =
                            component.favoritesComponent.onFavorite(id, isFavorite)

                    },
                )
            }

            item {
                PlayerListItem(playerComponent = component.playerComponent)
            }
        }
    }
}
