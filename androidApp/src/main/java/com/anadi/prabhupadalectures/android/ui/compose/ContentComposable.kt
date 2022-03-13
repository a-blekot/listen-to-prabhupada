package com.anadi.prabhupadalectures.android.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.anadi.prabhupadalectures.repository.Repository

@Composable
fun ContentComposable(
    repository: Repository,
    uiListener: ((UIAction) -> Unit)? = null
) {
    val state = repository.observeState().collectAsState()
    val playbackState = repository.observePlaybackState().collectAsState()

    Box(
        Modifier
            .background(color = MaterialTheme.colors.surface)
            .padding(all = 12.dp)
    ) {

        val isNotEmpty = !state.value.playlist.isEmpty()

        LazyColumn(
            Modifier.fillMaxWidth()
        ) {
            item { Header() }
            item { FilterButton() }

            items(state.value.playlist.lectures, key = { it.id }) { lecture ->
                playbackState.value.run {
                    LectureListItem(
                        lecture = lecture,
                        isPlaying = lecture.id == lectureId && isPlaying,
                        uiListener = uiListener
                    )
                }
            }

            item { PageControl(1, 309) }

            items(state.value.filters, key = { it.name }) { FilterListItem(it, repository, uiListener) }

            item { PlayerListItem(playbackState.value, uiListener) }
        }
    }
}