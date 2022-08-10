package com.listentoprabhupada.android_ui.screens.filters

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetpack.subscribeAsState
import com.listentoprabhupada.android_ui.LoadingBar
import com.listentoprabhupada.android_ui.custom.StandartColumn
import com.listentoprabhupada.android_ui.custom.StandartLazyColumn
import com.listentoprabhupada.android_ui.helpers.FilterListItem
import com.listentoprabhupada.android_ui.helpers.SelectedFilters
import com.listentoprabhupada.android_ui.theme.Colors.primary
import com.listentoprabhupada.android_ui.theme.Dimens.horizontalScreenPadding
import com.listentoprabhupada.android_ui.theme.Dimens.iconSizeXXL
import com.listentoprabhupada.android_ui.theme.Dimens.paddingM
import com.listentoprabhupada.android_ui.theme.Dimens.paddingXS
import com.listentoprabhupada.common.filters_api.FiltersComponent

@Composable
fun FiltersView(component: FiltersComponent, modifier: Modifier = Modifier) {
    val state = component.models.subscribeAsState()

    val expandedList = state.value.filters.map {
        remember { mutableStateOf(it.isExpanded) }
    }

    Box(modifier) {
        StandartLazyColumn(modifier = modifier) {
            item {
                SelectedFilters(state.value.filters, component, modifier)
            }

            itemsIndexed(state.value.filters, key = { _, item -> item.name }) { i, filter ->
                FilterListItem(filter, component, expandedList[i], modifier)
            }
        }

        IconButton(
            component::onApplyChanges,
            Modifier.padding(paddingM).size(iconSizeXXL).align(Alignment.BottomEnd)
        ) {
            Icon(
                Icons.Rounded.CheckCircle,
                "apply",
                Modifier.fillMaxSize(),
                tint = primary()
            )
        }

        if (state.value.isLoading) {
            LoadingBar()
        }
    }
}
