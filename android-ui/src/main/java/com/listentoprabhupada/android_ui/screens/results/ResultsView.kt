package com.listentoprabhupada.android_ui.screens.results

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetpack.subscribeAsState
import com.listentoprabhupada.android_ui.LoadingBar
import com.listentoprabhupada.android_ui.custom.StandartLazyColumn
import com.listentoprabhupada.android_ui.helpers.*
import com.listentoprabhupada.android_ui.theme.Dimens.bottomSheetPeekHeight
import com.listentoprabhupada.android_ui.theme.Dimens.paddingM
import com.listentoprabhupada.common.feature_results_api.ResultsFeatureComponent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ResultsView(component: ResultsFeatureComponent, modifier: Modifier = Modifier) =
    component.run {
        val state = resultsComponent.flow.subscribeAsState()

        val bottomSheetState = rememberBottomSheetScaffoldState(
            bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
        )
        val coroutineScope = rememberCoroutineScope()

        BottomSheetScaffold(
            scaffoldState = bottomSheetState,
            sheetContent = {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                ) {
                    PlayerListItem(playerComponent, modifier)
                }
            }, sheetPeekHeight = bottomSheetPeekHeight
        ) {

            Box(modifier.padding(bottom = bottomSheetPeekHeight)) {
                StandartLazyColumn(itemPadding = paddingM) {
                    item { Header(modifier = modifier) }

                    if (state.value.useSimplePageView) {
                        item { SimplePageControl(state.value.pagination, resultsComponent) }
                    } else {
                        item { PageControl(state.value.pagination, resultsComponent) }
                    }

                    items(state.value.lectures, key = { it.id }) { lectureItem ->
                        LectureListItem(lectureItem, resultsComponent, modifier)
                    }
                }

                if (state.value.isLoading) {
                    LoadingBar()
                }
            }
        }
    }

@OptIn(ExperimentalMaterialApi::class)
private suspend fun BottomSheetScaffoldState.toggleBottomSheet() =
    if (bottomSheetState.isCollapsed) {
        bottomSheetState.expand()
    } else {
        bottomSheetState.collapse()
    }
