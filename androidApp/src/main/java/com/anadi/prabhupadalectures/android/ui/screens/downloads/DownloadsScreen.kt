package com.anadi.prabhupadalectures.android.ui.screens.downloads

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import cafe.adriel.voyager.androidx.AndroidScreen
import com.anadi.prabhupadalectures.android.base.viewmodel.WithViewModel
import io.github.aakira.napier.Napier

class DownloadsScreen : AndroidScreen() {

    @Composable
    override fun Content() {
//        val connection by connectivityState()

        val context = LocalContext.current
        WithViewModel<DownloadsViewModel>(
            onEffect = { effect ->
                Napier.e("Composable - onEffect")
                when (effect) {
                    is DownloadsEffect.Toast -> {
                        Napier.e("Composable - onEffect - Toast")
                        Toast.makeText(context, effect.message, Toast.LENGTH_LONG).show()
                    }
                }
            },
            start = { viewModel, onEvent ->
                Napier.e("DownloadsView - start")
                val state = viewModel.state.collectAsState().value
                DownloadsView(state.downloads, state.playback, onEvent = onEvent)
            }
        )
    }
}
