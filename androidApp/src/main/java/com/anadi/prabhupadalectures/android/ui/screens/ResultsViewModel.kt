package com.anadi.prabhupadalectures.android.ui.screens

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import com.anadi.prabhupadalectures.datamodel.QueryParam
import com.anadi.prabhupadalectures.network.api.Error
import com.anadi.prabhupadalectures.network.api.Progress
import com.anadi.prabhupadalectures.repository.DownloadsRepository
import com.anadi.prabhupadalectures.repository.PlaybackRepository
import com.anadi.prabhupadalectures.repository.ResultsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class PlayerViewEffects {
    data class Toast(val msg: String): PlayerViewEffects()
}

@SuppressLint("ComposableNaming")
@Composable
fun ResultsViewModel.collectEffect(
    doEffect: (suspend (effect: PlayerViewEffects) -> Unit),
) {
    val effectFlow = this.effects
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(effectFlow, lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            effects.collect { launch { doEffect(it) } }
        }
    }
}

@HiltViewModel
class ResultsViewModel @Inject constructor(
    private val resultsRepository: ResultsRepository,
    private val playbackRepository: PlaybackRepository,
    private val downloadsRepository: DownloadsRepository,
) : ViewModel() {

    val effects = MutableSharedFlow<PlayerViewEffects>()
    private var p = 0

    init {
        viewModelScope.launch {
            resultsRepository.init()
        }

        viewModelScope.launch {
            downloadsRepository.observeState()
                .onEach {
                    when (it) {
                        is Progress -> {
                            if (it.progress - p > 10) {
                                effects.emit(PlayerViewEffects.Toast("Progress = ${it.progress}%"))
                            }
                            p = it.progress
                        }
                        is Error -> { effects.emit(PlayerViewEffects.Toast("Error: ${it.t?.message}")) }
                        else -> { /** do nothing **/ }
                    }
                }
                .collect()
        }
    }

    fun observeResults() = resultsRepository.observeState()
    fun observePlayback() = playbackRepository.observeState()
    fun observeEffects() = effects.asSharedFlow()
}