package com.listentoprabhupada.android.ui.screens.filters

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetpack.subscribeAsState
import com.listentoprabhupada.android.ui.LoadingBar
import com.listentoprabhupada.android.ui.screens.helpers.FilterListItem
import com.listentoprabhupada.android.ui.screens.helpers.SelectedFilters
import com.listentoprabhupada.common.filters_api.FiltersComponent

@Composable
fun FiltersView(component: FiltersComponent) {
    val state = component.models.subscribeAsState()

    val expandedList = state.value.filters.map {
        remember { mutableStateOf(it.isExpanded) }
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.surface)
    ) {
        Column(Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .weight(100f)
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 4.dp),
            ) {

                item {
                    SelectedFilters(
                        component = component,
                        filters = state.value.filters,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                itemsIndexed(state.value.filters, key = { _, item -> item.name }) { i, filter ->
                    FilterListItem(component, filter, expandedList[i])
                }
            }


            Button(
                onClick = component::onApplyChanges,
                modifier = Modifier
                    .weight(10f)
                    .align(Alignment.CenterHorizontally)
                    .padding(all = 12.dp)
            ) {
                Text("Посмотреть результаты (${state.value.totalLecturesCount})")
            }
        }

        if (state.value.isLoading) {
            LoadingBar()
        }
    }
}


