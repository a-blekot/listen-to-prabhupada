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
import cafe.adriel.voyager.core.screen.Screen
import com.anadi.prabhupadalectures.android.PrabhupadaApp.Companion.app
import com.anadi.prabhupadalectures.repository.Repository

class MainScreen(
    private val repository : Repository,
    private val uiListener: ((UIAction) -> Unit)? = null
) : Screen {

    @Composable
    override fun Content() {
        val state = repository.observeState().collectAsState()
        val playbackState = repository.observePlaybackState().collectAsState()

        Box(
            Modifier
                .background(color = MaterialTheme.colors.surface)
                .padding(all = 12.dp)) {

            LazyColumn(
                Modifier.fillMaxWidth()
            ) {
                val isNotEmpty = !state.value.playlist.isEmpty()

                if (isNotEmpty) {
                    item { Header() }
                    item { FilterButton() }
                }

                items(state.value.playlist.lectures, key = { it.id }) { lecture ->
                    playbackState.value.run {
                        LectureListItem(
                            lecture = lecture,
                            isPlaying = lecture.id == lectureId && isPlaying,
                            uiListener = uiListener
                        )
                    }
                }

                if (isNotEmpty) {
                    item { PageControl(1, 309) }
                }

                items(state.value.filters) { FilterListItem(it, uiListener) }

                item { PlayerListItem(playbackState.value, uiListener) }
            }
        }
    }
}
