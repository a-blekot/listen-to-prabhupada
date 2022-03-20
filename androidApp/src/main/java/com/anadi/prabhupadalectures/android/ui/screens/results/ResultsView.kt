package com.anadi.prabhupadalectures.android.ui.screens.results

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.anadi.prabhupadalectures.android.ui.compose.*
import com.anadi.prabhupadalectures.repository.PlaybackState
import com.anadi.prabhupadalectures.repository.ResultsState
import kotlinx.coroutines.launch

@Preview
@Composable
fun ResultsView(
    resultsState: ResultsState = ResultsState(),
    playbackState: PlaybackState = PlaybackState(),
    isOnline: Boolean = true,
    onEvent: (ResultsEvent) -> Unit = {}
) {
    Box(
        Modifier
            .background(color = MaterialTheme.colors.surface)
            .padding(all = 12.dp)
    )
    {
        val scaffoldState: ScaffoldState = rememberScaffoldState()
        val coroutineScope = rememberCoroutineScope()

        Scaffold(
            scaffoldState = scaffoldState,
            drawerContent = { Text("Drawer content") },
            bottomBar = {
                BottomAppBar(cutoutShape = CircleShape) {
                    IconButton(
                        onClick = {
                            coroutineScope.launch { scaffoldState.drawerState.open() }
                        }
                    ) {
                        Icon(Icons.Filled.Menu, contentDescription = "....")
                    }
                }
            }
        ) {
            Box {
                val isNotEmpty = resultsState.lectures.isNotEmpty()

                LazyColumn(
                    Modifier.fillMaxWidth()
                ) {

                    if (isNotEmpty) {

//                        item { Header(resultsState.lecturesCount) }

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
                                resultsState.lecturesCount,
                                onEvent = onEvent
                            )
                        }
                    }

                    items(resultsState.filters, key = { it.name }) { FilterListItem(it, onEvent = onEvent) }

                    item { PlayerListItem(playbackState, onEvent) }
                }

                if (resultsState.isLoading) {
                    LoadingBar()
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewLoadingBar() =
    LoadingBar()

@Composable
fun LoadingBar() =
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator()
    }

