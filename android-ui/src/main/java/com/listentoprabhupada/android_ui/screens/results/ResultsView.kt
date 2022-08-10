package com.listentoprabhupada.android_ui.screens.results

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetpack.subscribeAsState
import com.listentoprabhupada.android.ui.LoadingBar
import com.listentoprabhupada.android_ui.custom.StandartLazyColumn
import com.listentoprabhupada.android_ui.helpers.*
import com.listentoprabhupada.android_ui.theme.Dimens.paddingM
import com.listentoprabhupada.common.feature_results_api.ResultsComponent

@Composable
fun ResultsView(component: ResultsComponent, modifier: Modifier = Modifier) =
    component.run {
        val state = resultsComponent.flow.subscribeAsState()

        Box(Modifier.background(color = colorScheme.background)) {
            StandartLazyColumn(itemPadding = paddingM) {
                item { Header(modifier = modifier) }

                items(state.value.lectures, key = { it.id }) { lectureItem ->
                    LectureListItem(lectureItem, resultsComponent, modifier)
                }

                item { PageControl(state.value.pagination, resultsComponent, modifier) }
                item { PlayerListItem(playerComponent, modifier) }
            }

            if (state.value.isLoading) {
                LoadingBar()
            }
        }
    }
