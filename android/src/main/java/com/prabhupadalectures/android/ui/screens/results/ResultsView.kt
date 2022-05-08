package com.prabhupadalectures.android.ui.screens.results

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetpack.subscribeAsState
import com.prabhupadalectures.android.ui.LoadingBar
import com.prabhupadalectures.android.ui.compose.OfflineComposable
import com.prabhupadalectures.android.ui.screens.helpers.DrawerContent
import com.prabhupadalectures.android.ui.screens.helpers.Header
import com.prabhupadalectures.android.ui.screens.helpers.LectureListItem
import com.prabhupadalectures.android.ui.screens.helpers.PageControl
import com.prabhupadalectures.lectures.events.CommonUiEvent
import com.prabhupadalectures.lectures.mvi.lectures.Results
import kotlinx.coroutines.launch

@Composable
fun ResultsView(
    component: Results,
    isFiltersHeaderExpanded: Boolean = true,
) {

    val state = component.models.subscribeAsState()
    val resultsState = state.value

    val isFiltersHeaderExpandedRemember = remember { mutableStateOf(isFiltersHeaderExpanded) }

    val onEvent: (CommonUiEvent) -> Unit = {
        when (it) {
            is CommonUiEvent.ResultsEvent.Page ->
                component.onPage(it.page)

//            is CommonUiEvent.ResultsEvent.Option ->
//                component.onQueryParam(it.queryParam)

            else -> {}
        }
    }


    if (true) { // isOnline
        val scaffoldState: ScaffoldState = rememberScaffoldState()
        val coroutineScope = rememberCoroutineScope()

        Scaffold(
            scaffoldState = scaffoldState,
            drawerContent = {
                DrawerContent {
                    when (it) {
                        CommonUiEvent.ResultsEvent.OpenFavorites,
                        CommonUiEvent.ResultsEvent.OpenDownloads -> {
                            coroutineScope.launch {
                                scaffoldState.drawerState.close()
                            }
                        }
                        else -> {
                            /** do nothing **/
                            /** do nothing **/
                        }
                    }
                    onEvent(it)
                }
            },
            topBar = {
                TopAppBar(component = component) {
                    coroutineScope.launch {
                        scaffoldState.drawerState.toggle()
                    }
                }
            },
            bottomBar = {
                // PlayerListItem(playbackState, onEvent)
            }
        ) {
            val a = it.calculateBottomPadding()

            Box(
                Modifier
                    .background(color = MaterialTheme.colors.surface)
            ) {
                val isNotEmpty = resultsState.lectures.isNotEmpty()

                // Remember our own LazyListState
//                val listState = rememberLazyListState()
//                val coroutineScope = rememberCoroutineScope()

//                Show the button if the first visible item is past
//                    the first item.We use a remembered derived state to
//                minimize unnecessary compositions
//                    val showButton by remember { mutableStateOf(listState.firstVisibleItemIndex > 0)
////                        derivedStateOf {
////                            listState.firstVisibleItemIndex > 0
////                        }
//                    }
//                    AnimatedVisibility(visible = true) {
//                        Button(
//                            onClick = {
//                                coroutineScope.launch {
//                                    // Animate scroll to the first item
//                                    listState.animateScrollToItem(index = 0)
//                                }
//                            }
//                        ) {
//                            Text("asd")
//                        }
//                    }

//                val expandedList = resultsState.filters.map {
//                    remember { mutableStateOf(it.isExpanded) }
//                }

                LazyColumn(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .fillMaxWidth(),
//                        state = listState,
                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 40.dp),
//                        verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    if (isNotEmpty) {
                        item {
                            Header(
                                onEvent = onEvent,
                                onMenuClick = {
                                    coroutineScope.launch {
                                        scaffoldState.drawerState.toggle()
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.Center)
                            )
                        }

                        items(resultsState.lectures, key = { it.id }) { lectureItem ->
//                            playbackState.run {
                                LectureListItem(
                                    lecture = lectureItem,
                                    isPlaying = false, //lectureItem.id == lecture.id && isPlaying,
                                    onEvent = onEvent
                                )
//                            }
                        }

                        item {
                            PageControl(
                                resultsState.pagination,
                                onEvent = onEvent
                            )
                        }
                    }

//                    item {
//                        SelectedFilters(
//                            filters = resultsState,
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(top = 12.dp),
//                            onEvent = onEvent
//                        )
//                    }

//                    item {
//                        FilterTitle(filtersHeader, isFiltersHeaderExpandedRemember.value) {
//                            isFiltersHeaderExpandedRemember.value = it
//                            onEvent(CommonUiEvent.ResultsEvent.Expand(filtersHeader.name, it))
//                        }
//                    }
//
//                    if (isFiltersHeaderExpandedRemember.value) {
//                        itemsIndexed(resultsState.filters, key = { _, item -> item.name }) { i, filter ->
//                            FilterListItem(filter, expandedList[i], onEvent = onEvent)
//                        }
//                    }

//                    item {
//                        PlayerListItem(playbackState, onEvent)
//                    }
                }

                if (resultsState.isLoading) {
                    LoadingBar()
                }
            }
        }
    } else {
        OfflineComposable(onEvent)
    }
}

@Composable
private fun TopAppBar(component: Results, onMenuClick: () -> Unit = {}) {
    TopAppBar(
        actions = {
            IconButton(
                onClick = { component.onEditFilters() },
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .wrapContentWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.List,
//                    modifier = Modifier.padding(horizontal = 8.dp),
                    contentDescription = "Filter icon"
                )
            }

            IconButton(
                onClick = { onMenuClick() },
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .wrapContentWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
//                    modifier = Modifier.padding(horizontal = 8.dp),
                    contentDescription = "Menu icon"
                )
            }
        },
        title = { Text("") },
        backgroundColor = MaterialTheme.colors.background
    )
}

private suspend fun DrawerState.toggle() =
    if (isOpen) close() else open()