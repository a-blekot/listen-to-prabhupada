package com.listentoprabhupada.android_ui.screens.results

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetpack.subscribeAsState
import com.listentoprabhupada.android_ui.LoadingBar
import com.listentoprabhupada.android_ui.custom.StandartLazyColumn
import com.listentoprabhupada.android_ui.helpers.*
import com.listentoprabhupada.android_ui.theme.Dimens.paddingM
import com.listentoprabhupada.common.results_api.ResultsComponent

@Composable
fun ResultsView(component: ResultsComponent, modifier: Modifier = Modifier) {
    val state = component.flow.subscribeAsState()
    Box(modifier) {
        StandartLazyColumn(itemPadding = paddingM) {
            item { Header() }

            if (state.value.useSimplePageView) {
                item { SimplePageControl(state.value.pagination, component) }
            } else {
                item { PageControl(state.value.pagination, component) }
            }

            items(state.value.lectures, key = { it.id }) { lectureItem ->
                LectureListItem(lectureItem, component)
            }
        }

        if (state.value.isLoading) {
            LoadingBar()
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
