package com.listentoprabhupada.common.favorites_impl

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.listentoprabhupada.common.data.LectureOutput
import com.listentoprabhupada.common.favorites_api.FavoritesComponent
import com.listentoprabhupada.common.favorites_api.FavoritesState
import com.listentoprabhupada.common.favorites_impl.store.FavoritesIntent.CurrentLecture
import com.listentoprabhupada.common.favorites_impl.store.FavoritesIntent.Favorite
import com.listentoprabhupada.common.favorites_impl.store.FavoritesLabel
import com.listentoprabhupada.common.favorites_impl.store.FavoritesStoreFactory
import com.listentoprabhupada.common.utils.Consumer
import com.listentoprabhupada.common.utils.asValue
import com.listentoprabhupada.common.utils.getStore
import com.listentoprabhupada.common.utils.lifecycleCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


class FavoritesComponentImpl(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    deps: FavoritesDeps,
    private val output: Consumer<LectureOutput>
) : FavoritesComponent, ComponentContext by componentContext {

    private val store =
        instanceKeeper.getStore {
            FavoritesStoreFactory(
                storeFactory = storeFactory,
                deps = deps,
            ).create()
        }

    private val scope: CoroutineScope = lifecycleCoroutineScope(deps.dispatchers.main)

    override val flow: Value<FavoritesState> = store.asValue()

    init {
        store.labels
            .onEach(::handleLabel)
            .launchIn(scope)

        store.init()
    }

    override fun onPlay(id: Long) = output(LectureOutput.Play(id))
    override fun onPause() = output(LectureOutput.Pause)
    override fun onFavorite(id: Long, isFavorite: Boolean) = store.accept(Favorite(id = id, isFavorite = isFavorite))
    override fun onCurrentLecture(id: Long, isPlaying: Boolean) = store.accept(CurrentLecture(id, isPlaying))

    private fun handleLabel(label: FavoritesLabel) {
        when (label) {
            is FavoritesLabel.LecturesLoaded -> output(LectureOutput.UpdatePlaylist(label.lectures))
        }
    }
}
