package com.anadi.prabhupadalectures.android.ui.screens.results

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import cafe.adriel.voyager.androidx.AndroidScreen
import com.anadi.prabhupadalectures.android.viewmodel.WithViewModel
import io.github.aakira.napier.Napier

class ResultsScreen : AndroidScreen() {

    @Composable
    override fun Content() {
//        val connection by connectivityState()

        val context = LocalContext.current
        WithViewModel<ResultsViewModel>(
            onEffect = { effect ->
                Napier.e("Composable - onEffect")
                when (effect) {
                    is ResultsEffect.Toast -> {
                        Napier.e("Composable - onEffect - Toast")
                        Toast.makeText(context, effect.message, Toast.LENGTH_LONG).show()
                    }
                }
            },
            start = { viewModel, onEvent ->
                Napier.e("ResultsView - start")
                val state = viewModel.state.collectAsState().value
                ResultsView(state.results, state.playback, onEvent = onEvent)
            }
        )
    }
}
