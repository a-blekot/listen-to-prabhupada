package com.listentoprabhupada.android.ui.screens.results

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material.icons.rounded.MenuOpen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetpack.subscribeAsState
import com.listentoprabhupada.android.R
import com.listentoprabhupada.android.ui.LoadingBar
import com.listentoprabhupada.android.ui.screens.helpers.*
import com.listentoprabhupada.common.feature_results_api.ResultsComponent
import kotlinx.coroutines.launch

@Composable
fun ResultsView(component: ResultsComponent) {
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
//                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 40.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
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
                                component = object: Listener {
                                    override fun onPause() = component.lecturesComponent.onPause()

                                    override fun onPlay(id: Long) = component.lecturesComponent.onPlay(id)

                                    override fun onFavorite(id: Long, isFavorite: Boolean) =
                                        component.lecturesComponent.onFavorite(id, isFavorite)

                                },
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
            Button(
                onClick = { component.onEditFilters() },
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .wrapContentWidth()
            ) {
                Icon(
                    imageVector = Icons.Rounded.FilterList,
//                    modifier = Modifier.padding(horizontal = 8.dp),
                    contentDescription = "Filter icon"
                )
            }

//            IconButton(
//                onClick = { component.onShowFavorites() },
//                modifier = Modifier
//                    .padding(horizontal = 8.dp)
//                    .wrapContentWidth()
//            ) {
//                Icon(
//                    imageVector = Icons.Rounded.Favorite,
////                    modifier = Modifier.padding(horizontal = 8.dp),
//                    contentDescription = "Favorites icon"
//                )
//            }

            IconButton(
                onClick = { onMenuClick() },
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .wrapContentWidth()
            ) {
                Icon(
                    imageVector = Icons.Rounded.MenuOpen,
//                    modifier = Modifier.padding(horizontal = 8.dp),
                    contentDescription = "Menu icon"
                )
            }
        },
        title = {
            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_logo),
                    colorFilter = ColorFilter.tint(MaterialTheme.colors.onPrimary),
                    contentDescription = "logo image",
                    modifier = Modifier
                        .height(50.dp)
                        .aspectRatio(1f)
                        .align(Alignment.CenterVertically)
                )

                Text(
                    text = stringResource(id = R.string.header_app_name),
                    style = MaterialTheme.typography.h6,
                    maxLines = 2,
                    textAlign = TextAlign.Start,
                    color = MaterialTheme.colors.onSecondary,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                )
            }
        },
        backgroundColor = MaterialTheme.colors.background
    )
}

private suspend fun DrawerState.toggle() =
    if (isOpen) close() else open()
