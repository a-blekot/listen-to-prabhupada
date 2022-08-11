package com.listentoprabhupada.android_ui.screens.results

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetpack.subscribeAsState
import com.listentoprabhupada.android_ui.LoadingBar
import com.listentoprabhupada.android_ui.custom.StandartLazyColumn
import com.listentoprabhupada.android_ui.helpers.*
import com.listentoprabhupada.android_ui.theme.Dimens.paddingM
import com.listentoprabhupada.common.feature_results_api.ResultsFeatureComponent

@Composable
fun ResultsView(component: ResultsFeatureComponent, modifier: Modifier = Modifier) =
    component.run {
        val state = resultsComponent.flow.subscribeAsState()

        Box(modifier) {
            StandartLazyColumn(itemPadding = paddingM) {
                item { Header(modifier = modifier) }

                item { PageControl(state.value.pagination, resultsComponent) }

                items(state.value.lectures, key = { it.id }) { lectureItem ->
                    LectureListItem(lectureItem, resultsComponent, modifier)
                }

                item { PlayerListItem(playerComponent, modifier) }
            }

            if (state.value.isLoading) {
                LoadingBar()
            }
        }
    }
