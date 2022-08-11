package com.listentoprabhupada.android_ui.screens.filters

import android.graphics.Paint.Align
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetpack.subscribeAsState
import com.listentoprabhupada.android_ui.LoadingBar
import com.listentoprabhupada.android_ui.custom.StandartLazyColumn
import com.listentoprabhupada.android_ui.helpers.FilterListItem
import com.listentoprabhupada.android_ui.helpers.SelectedFilters
import com.listentoprabhupada.android_ui.theme.Colors.filtersCategory
import com.listentoprabhupada.android_ui.theme.Colors.filtersText
import com.listentoprabhupada.android_ui.theme.Dimens.horizontalScreenPadding
import com.listentoprabhupada.android_ui.theme.Dimens.iconSizeM
import com.listentoprabhupada.android_ui.theme.Dimens.paddingS
import com.listentoprabhupada.android_ui.theme.Dimens.paddingXS
import com.listentoprabhupada.android_ui.theme.Dimens.rowHeightM
import com.listentoprabhupada.common.filters_api.FiltersComponent

@Composable
fun FiltersView(component: FiltersComponent, modifier: Modifier = Modifier) {
    val state = component.models.subscribeAsState()

    val expandedList = state.value.filters.map {
        remember { mutableStateOf(it.isExpanded) }
    }

    Box(modifier) {
        StandartLazyColumn(modifier = Modifier.padding(bottom = rowHeightM + paddingXS)) {
            item {
                SelectedFilters(state.value.filters, component, Modifier.fillMaxWidth())
            }

            itemsIndexed(state.value.filters, key = { _, item -> item.name }) { i, filter ->
                FilterListItem(filter, component, expandedList[i], Modifier.fillMaxWidth())
            }
        }

        Button(
            onClick = component::onApplyChanges,
            modifier = Modifier
                .fillMaxWidth()
                .height(rowHeightM)
                .padding(horizontal = horizontalScreenPadding)
                .padding(bottom = paddingXS)
                .align(Alignment.BottomCenter),
            colors = ButtonDefaults.buttonColors(
                containerColor = filtersCategory(),
                contentColor = filtersText()
            )
        ) {
            Icon(Icons.Rounded.CheckCircle, "apply", Modifier.size(iconSizeM))

            Spacer(Modifier.width(paddingS))

            Text(
                "Посмотреть результаты (${state.value.totalLecturesCount})",
                style = MaterialTheme.typography.titleMedium
            )
        }

        if (state.value.isLoading) {
            LoadingBar()
        }
    }
}
