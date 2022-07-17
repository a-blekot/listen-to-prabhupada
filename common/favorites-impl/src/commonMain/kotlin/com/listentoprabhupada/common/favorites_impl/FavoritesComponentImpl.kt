package com.listentoprabhupada.common.favorites_impl

import co.touchlab.stately.ensureNeverFrozen
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.listentoprabhupada.common.favorites_api.FavoritesComponent
import com.listentoprabhupada.common.favorites_api.FavoritesOutput
import com.listentoprabhupada.common.favorites_api.FavoritesState
import com.listentoprabhupada.common.favorites_impl.store.FavoritesIntent.*
import com.listentoprabhupada.common.favorites_impl.store.FavoritesLabel
import com.listentoprabhupada.common.favorites_impl.store.FavoritesStoreFactory
import com.listentoprabhupada.common.utils.Consumer
import com.listentoprabhupada.common.utils.asValue
import com.listentoprabhupada.common.utils.getStore
import com.listentoprabhupada.common.utils.lifecycleCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.coroutines.CoroutineContext
import io.github.aakira.napier.Napier


class FavoritesComponentImpl(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    deps: FavoritesDeps,
    private val output: Consumer<FavoritesOutput>
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

        flow.ensureNeverFrozen()

        store.init()
    }

    override fun onPlay(id: Long) = output(FavoritesOutput.Play(id))
    override fun onPause() = output(FavoritesOutput.Pause)
    override fun onFavorite(id: Long, isFavorite: Boolean) = store.accept(Favorite(id = id, isFavorite = isFavorite))
    override fun onCurrentLecture(id: Long, isPlaying: Boolean) = store.accept(CurrentLecture(id, isPlaying))

    private fun handleLabel(label: FavoritesLabel) {
        when (label) {
            is FavoritesLabel.LecturesLoaded -> output(FavoritesOutput.UpdatePlaylist(label.lectures))
        }
    }
}
