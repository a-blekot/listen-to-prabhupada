package com.anadi.prabhupadalectures.android.ui.compose

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.anadi.prabhupadalectures.android.DebugLog
import com.anadi.prabhupadalectures.android.ui.screens.PlayerViewEffects
import com.anadi.prabhupadalectures.android.ui.screens.ResultsViewModel
import com.anadi.prabhupadalectures.android.ui.screens.collectEffect
import com.anadi.prabhupadalectures.repository.ResultsRepository
import com.anadi.prabhupadalectures.repository.PlaybackRepository
import com.anadi.prabhupadalectures.repository.ToolsRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun ContentComposable(
    resultsViewModel: ResultsViewModel = viewModel()
) {
    val context = LocalContext.current
    val resultsState = resultsViewModel.observeResults().collectAsState()
    val playbackState = resultsViewModel.observePlayback().collectAsState()

    resultsViewModel.collectEffect {effect ->
        DebugLog.d("PlayerViewEffects", "collectEffect")
        when (effect) {
            is PlayerViewEffects.Toast -> {
                DebugLog.d("PlayerViewEffects", "Toast ${effect.msg}")
                Toast.makeText(context, effect.msg, Toast.LENGTH_LONG).apply{}.show()
            }
        }
    }

    Box(
        Modifier
            .background(color = MaterialTheme.colors.surface)
            .padding(all = 12.dp)
    ) {

        val isNotEmpty = resultsState.value.lectures.isNotEmpty()

        if (isNotEmpty) {
            LazyColumn(
                Modifier.fillMaxWidth()
            ) {
                item { Header(resultsState.value.lecturesCount) }
                item { FilterButton() }

                items(resultsState.value.lectures, key = { it.id }) { lectureItem ->
                    playbackState.value.run {
                        LectureListItem(
                            lecture = lectureItem,
                            isPlaying = lectureItem.id == lecture.id && isPlaying
                        )
                    }
                }

                item { PageControl(1, 309) }

                items(resultsState.value.filters, key = { it.name }) { FilterListItem(it) }

                item { PlayerListItem() }
            }
        }
    }
}