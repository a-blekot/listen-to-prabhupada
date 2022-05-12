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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetpack.subscribeAsState
import com.prabhupadalectures.android.ui.LoadingBar
import com.prabhupadalectures.android.ui.screens.helpers.*
import com.prabhupadalectures.common.feature_results_api.ResultsComponent
import kotlinx.coroutines.launch

@Composable
fun ResultsView(
    component: ResultsComponent,
) {
    val lecturesState = component.lecturesComponent.flow.subscribeAsState()

    if (true) { // isOnline
        val scaffoldState: ScaffoldState = rememberScaffoldState()
        val coroutineScope = rememberCoroutineScope()

        Scaffold(
            scaffoldState = scaffoldState,
            drawerContent = {
                DrawerContent(component)
//                    coroutineScope.launch {
//                        scaffoldState.drawerState.close()
//                    }
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
                        item {
                            Header(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.Center)
                            )
                        }

                        items(lecturesState.value.lectures, key = { it.id }) { lectureItem ->
                            LectureListItem(
                                lecture = lectureItem,
                                component = component.lecturesComponent,
                            )
                        }

                        item {
                            PageControl(
                                component.lecturesComponent,
                                lecturesState.value.pagination,
                            )
                        }

                    item {
                        PlayerListItem(playerComponent = component.playerComponent)
                    }
                }

                if (lecturesState.value.isLoading) {
                    LoadingBar()
                }
            }
        }
    } else {
        OfflineComposable()
    }
}

@Composable
private fun TopAppBar(component: ResultsComponent, onMenuClick: () -> Unit = {}) {
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
