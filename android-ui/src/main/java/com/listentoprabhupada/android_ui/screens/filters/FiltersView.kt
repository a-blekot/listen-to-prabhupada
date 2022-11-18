package com.listentoprabhupada.android_ui.screens.filters

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import com.arkivanov.decompose.extensions.compose.jetpack.subscribeAsState
import com.listentoprabhupada.android_ui.LoadingBar
import com.listentoprabhupada.android_ui.custom.StandartLazyColumn
import com.listentoprabhupada.android_ui.helpers.FilterListItem
import com.listentoprabhupada.android_ui.helpers.SelectedFilters
import com.listentoprabhupada.android_ui.theme.Colors.filtersText
import com.listentoprabhupada.android_ui.theme.Colors.toolbar
import com.listentoprabhupada.android_ui.theme.Dimens.iconSizeM
import com.listentoprabhupada.android_ui.theme.Dimens.paddingL
import com.listentoprabhupada.android_ui.theme.Dimens.paddingM
import com.listentoprabhupada.android_ui.theme.Dimens.paddingS
import com.listentoprabhupada.android_ui.theme.Dimens.toolbarHeightL
import com.listentoprabhupada.common.filters_api.FiltersComponent

@Composable
fun FiltersView(component: FiltersComponent, modifier: Modifier = Modifier) {
    val state = component.models.subscribeAsState()

    val expandedList = state.value.filters.map {
        remember { mutableStateOf(it.isExpanded) }
    }

    Box(modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(toolbarHeightL)
                .background(color = toolbar())
                .padding(start = paddingS)
        ) {
            IconButton(onClick = component::onApplyChanges) {
                Icon(
                    Icons.Rounded.ArrowBack,
                    "apply",
                    Modifier.size(iconSizeM),
                    tint = filtersText()
                )
            }

            Spacer(Modifier.width(paddingL))

            Text(
                "Найдено: ${state.value.totalLecturesCount} лекций",
                style = typography.titleLarge,
                color = filtersText(),
            )
        }



        StandartLazyColumn(modifier = Modifier.padding(top = toolbarHeightL)) {
            item {
                SearchView(state.value.searchQuery) {
                    component.search(it)
                }
            }

            item {
                SelectedFilters(
                    state.value.filters,
                    component,
                    Modifier.fillMaxWidth().padding(bottom = paddingM)
                )
            }

            itemsIndexed(state.value.filters, key = { _, item -> item.name }) { i, filter ->
                FilterListItem(filter, component, expandedList[i], Modifier.fillMaxWidth())
            }
        }

        if (state.value.isLoading) {
            LoadingBar()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchView(initialText: String, onChanged: (String) -> Unit) {
    val text = remember { mutableStateOf(TextFieldValue(text = initialText)) }

    TextField(
        value = text.value,
        onValueChange = { value ->
            text.value = value
            onChanged(value.text)
        },
        modifier = Modifier.fillMaxWidth(),
        textStyle = typography.titleMedium,
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = "",
                modifier = Modifier
                    .padding(paddingS)
                    .size(iconSizeM)
            )
        },
        trailingIcon = {
            if (text.value != TextFieldValue("")) {
                IconButton(
                    onClick = {
                        text.value = TextFieldValue("") // Remove text from TextField when you press the 'X' icon
                        onChanged("")
                    }
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "",
                        modifier = Modifier
                            .padding(paddingS)
                            .size(iconSizeM)
                    )
                }
            }
        },
        singleLine = true,
//        shape = RectangleShape, // The TextFiled has rounded corners top left and right by default
//        colors = TextFieldDefaults.textFieldColors(
//            textColor = Color.White,
//            cursorColor = Color.White,
//            disabledLeadingIconColor = Color.White,
//            trailingIconColor = Color.White,
//            backgroundColor = MaterialTheme.colors.primary,
//            focusedIndicatorColor = Color.Transparent,
//            unfocusedIndicatorColor = Color.Transparent,
//            disabledIndicatorColor = Color.Transparent
//        )
    )
}
