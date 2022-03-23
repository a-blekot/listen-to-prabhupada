package com.anadi.prabhupadalectures.android.ui.screens.results

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.anadi.prabhupadalectures.android.R
import com.anadi.prabhupadalectures.android.ui.compose.*
import com.anadi.prabhupadalectures.android.ui.screens.CommonUiEvent
import com.anadi.prabhupadalectures.network.api.Progress
import com.anadi.prabhupadalectures.repository.PlaybackState
import com.anadi.prabhupadalectures.repository.ResultsState
import kotlinx.coroutines.launch

@Preview
@Composable
fun ResultsView(
    resultsState: ResultsState = ResultsState(),
    playbackState: PlaybackState = PlaybackState(),
    isOnline: Boolean = true,
    onEvent: (CommonUiEvent) -> Unit = {}
) {
    if (isOnline) {
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
                        }
                    }
                    onEvent(it)
                }
            },
            topBar = {
                TopAppBar {
                    coroutineScope.launch {
                        scaffoldState.drawerState.toggle()
                    }
                }
            }
        ) {
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

                    LazyColumn(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .fillMaxWidth(),
//                        state = listState,
                        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 40.dp),
//                        verticalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        if (isNotEmpty) {
                            item { Header(modifier = Modifier.fillMaxWidth().align(Alignment.Center)) }

                            items(resultsState.lectures, key = { it.id }) { lectureItem ->
                                playbackState.run {
                                    LectureListItem(
                                        lecture = lectureItem,
                                        isPlaying = lectureItem.id == lecture.id && isPlaying,
                                        onEvent = onEvent
                                    )
                                }
                            }

                            item {
                                PageControl(
                                    resultsState.pagination,
                                    onEvent = onEvent
                                )
                            }
                        }

                        item {
                            SelectedFilters(
                                filters = resultsState.filters,
                                modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                                onEvent = onEvent
                            )
                        }

                        items(resultsState.filters, key = { it.name }) {
                            FilterListItem(it, onEvent = onEvent)
                        }

                        item {
                            PlayerListItem(playbackState, onEvent)
                        }
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
private fun TopAppBar(onMenuClick: () -> Unit = {}) {
    TopAppBar(
        actions = {
            IconButton(
                onClick = { onMenuClick() },
                modifier = Modifier.padding(horizontal = 8.dp).wrapContentWidth()
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

@Preview
@Composable
fun PreviewLoadingBar() =
    LoadingBar()

@Composable
fun LoadingBar(modifier: Modifier = Modifier) =
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .background(
                color = AlphaDarkBg,
                shape = RoundedCornerShape(4.dp)
            )
    ) {
        CircularProgressIndicator()
    }

private suspend fun DrawerState.toggle() =
    if (isOpen) close() else open()
