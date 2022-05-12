//package com.prabhupadalectures.android.ui.screens.downloads
//
//import android.content.Context
//import androidx.lifecycle.viewModelScope
//import com.prabhupadalectures.android.base.viewmodel.BaseViewModel
//import com.prabhupadalectures.common.lectures_impl.events.CommonUiEvent
//import com.prabhupadalectures.common.lectures_impl.repository.DownloadsRepository
//import com.prabhupadalectures.common.lectures_impl.repository.ToolsRepository
//import com.prabhupadalectures.common.player_api.PlayerAction
//import com.prabhupadalectures.common.player_api.PlayerBus
//import io.github.aakira.napier.Napier
//import kotlinx.coroutines.CoroutineDispatcher
//import kotlinx.coroutines.launch
//
//class DownloadsViewModel(
//    context: Context,
//    mainDispatcher: CoroutineDispatcher,
//    ioDispatcher: CoroutineDispatcher,
////    private val resultsRepository: ResultsRepository,
//    private val playerBus: PlayerBus,
//    private val downloadsRepository: DownloadsRepository,
//    private val toolsRepository: ToolsRepository,
//) : BaseViewModel<CommonUiEvent, DownloadsScreenState, DownloadsEffect>(context, mainDispatcher, ioDispatcher) {
//
//    init {
//        observeState()
//    }
//
//    override fun onCleared() {
//        super.onCleared()
////        resultsRepository.capturePlayback()
//    }
//
//    override fun setInitialState() = DownloadsScreenState()
//
//    override fun handleEvents(event: CommonUiEvent) {
//        Napier.e("DownloadsViewModel handleEvents")
//        viewModelScope.launch {
//            when (event) {
//                CommonUiEvent.TappedBack -> {
////                    router.pop()
//                }
//                is CommonUiEvent.Favorite -> toolsRepository.setFavorite(event.lecture, event.isFavorite)
//                is CommonUiEvent.Player -> {
//                    when (event.action) {
//                        is PlayerAction.Download -> downloadsRepository.download((event.action as PlayerAction.Download).lecture)
//                        else -> playerBus.update(event.action)
//                    }
//                }
//                else -> {
//                    /** do nothing**/
//                }
//            }
//        }
//    }
//
//    private fun observeState() =
//        viewModelScope.launch {
////
////            toolsRepository.observeDownloads().combine(
////                playerBus.observeState()
////            ) { downloads, playback ->
////                setState {
////                    copy(downloads = downloads, playback = playback)
////                }
////
////                if (state.value.downloads != downloads) {
////                    playerBus.updatePlaylist(downloads)
////                }
////
////                if (downloads.isEmpty()) {
////                    setEffect {
////                        DownloadsEffect.Toast("There is no downloads")
////                    }
////                }
////            }
////                .collect()
//        }
//}
