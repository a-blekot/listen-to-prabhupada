//package com.prabhupadalectures.android.ui.screens.favorites
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
//import kotlinx.coroutines.flow.collect
//import kotlinx.coroutines.flow.combine
//import kotlinx.coroutines.flow.onEach
//import kotlinx.coroutines.launch
//
//class FavoritesViewModel(
//    context: Context,
//    mainDispatcher: CoroutineDispatcher,
//    ioDispatcher: CoroutineDispatcher,
////    private val resultsRepository: ResultsRepository,
//    private val playerBus: PlayerBus,
//    private val downloadsRepository: DownloadsRepository,
//    private val toolsRepository: ToolsRepository,
//) : BaseViewModel<CommonUiEvent, FavoritesScreenState, FavoritesEffect>(context, mainDispatcher, ioDispatcher) {
//
//    init {
//        observeState()
//        observePlaylist()
//    }
//
//    override fun onCleared() {
//        super.onCleared()
////        resultsRepository.capturePlayback()
//    }
//
//    override fun setInitialState() = FavoritesScreenState()
//
//    override fun handleEvents(event: CommonUiEvent) {
//        Napier.e("handleEvents")
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
//
////            toolsRepository.observeFavorites().combine(
////                playerBus.observeState()
////            ) { favorites, playback ->
////                setState {
////                    copy(favorites = favorites, playback = playback)
////                }
////                if (favorites.isEmpty()) {
////                    setEffect {
////                        FavoritesEffect.Toast("There is no favorites")
////                    }
////                }
////            }
////                .collect()
//        }
//
//    private fun observePlaylist() =
//        viewModelScope.launch {
//            toolsRepository.observeFavorites()
//                .onEach {
//                    playerBus.update(it)
//                }
//                .collect()
//        }
//}
