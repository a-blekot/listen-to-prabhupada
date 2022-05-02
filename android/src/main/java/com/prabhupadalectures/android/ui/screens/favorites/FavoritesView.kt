package com.prabhupadalectures.android.ui.screens.favorites

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.prabhupadalectures.android.ui.screens.helpers.LectureListItem
import com.prabhupadalectures.android.ui.compose.PlayerListItem
import com.prabhupadalectures.lectures.events.CommonUiEvent
import com.prabhupadalectures.lectures.data.lectures.Lecture
import com.prabhupadalectures.lectures.repository.PlaybackState

@Preview
@Composable
fun FavoritesView(
    lectures: List<Lecture> = emptyList(),
    playbackState: PlaybackState = PlaybackState(),
    onEvent: (CommonUiEvent) -> Unit = {}
) {
    Box(
        Modifier
            .background(color = MaterialTheme.colors.surface)
            .padding(all = 12.dp)
    )
    {
        LazyColumn(
            Modifier.fillMaxWidth()
        ) {
            items(lectures, key = { it.id }) { lectureItem ->
                playbackState.run {
                    LectureListItem(
                        lecture = lectureItem,
                        isPlaying = lectureItem.id == lecture.id && isPlaying,
                        onEvent = onEvent
                    )
                }
            }

            item {
                PlayerListItem(playbackState, onEvent)
            }
        }
    }
}