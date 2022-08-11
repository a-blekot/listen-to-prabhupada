package com.listentoprabhupada.android_ui.screens.results

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetpack.subscribeAsState
import com.listentoprabhupada.android_ui.LoadingBar
import com.listentoprabhupada.android_ui.custom.StandartLazyColumn
import com.listentoprabhupada.android_ui.helpers.*
import com.listentoprabhupada.android_ui.theme.Dimens.bottomSheetPeekHeight
import com.listentoprabhupada.android_ui.theme.Dimens.paddingM
import com.listentoprabhupada.common.results_api.ResultsComponent

@Composable
fun ResultsView(component: ResultsComponent, modifier: Modifier = Modifier) {
    val state = component.flow.subscribeAsState()
    Box(modifier.padding(bottom = bottomSheetPeekHeight)) {
        StandartLazyColumn(itemPadding = paddingM) {
            item { Header(modifier = modifier) }

            if (state.value.useSimplePageView) {
                item { SimplePageControl(state.value.pagination, component) }
            } else {
                item { PageControl(state.value.pagination, component) }
            }

            items(state.value.lectures, key = { it.id }) { lectureItem ->
                LectureListItem(lectureItem, component, modifier)
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
