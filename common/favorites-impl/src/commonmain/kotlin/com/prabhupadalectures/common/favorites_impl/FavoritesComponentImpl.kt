package com.prabhupadalectures.common.favorites_impl

import co.touchlab.stately.ensureNeverFrozen
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.LifecycleOwner
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.prabhupadalectures.common.favorites_api.FavoritesComponent
import com.prabhupadalectures.common.favorites_api.FavoritesOutput
import com.prabhupadalectures.common.favorites_api.FavoritesState
import com.prabhupadalectures.common.favorites_impl.store.FavoritesIntent.*
import com.prabhupadalectures.common.favorites_impl.store.FavoritesLabel
import com.prabhupadalectures.common.favorites_impl.store.FavoritesStoreFactory
import com.prabhupadalectures.common.utils.Consumer
import com.prabhupadalectures.common.utils.asValue
import com.prabhupadalectures.common.utils.getStore
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
    override fun onDownload(id: Long) =
        flow.value
            .lectures
            .firstOrNull { id == id }
            ?.let { output(FavoritesOutput.Download(it)) }
            ?: Unit

    private fun handleLabel(label: FavoritesLabel) {
        when (label) {
            is FavoritesLabel.LecturesLoaded -> output(FavoritesOutput.UpdatePlaylist(label.lectures))
        }
    }
}

fun LifecycleOwner.lifecycleCoroutineScope(coroutineContext: CoroutineContext = Dispatchers.Main): CoroutineScope {
    val scope = CoroutineScope(coroutineContext)
    lifecycle.doOnDestroy(scope::cancel)

    return scope
}
