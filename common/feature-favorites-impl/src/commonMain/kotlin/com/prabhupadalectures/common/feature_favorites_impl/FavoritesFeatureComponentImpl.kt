package com.listentoprabhupada.common.feature_favorites_impl

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.listentoprabhupada.common.favorites_api.FavoritesComponent
import com.listentoprabhupada.common.favorites_api.FavoritesOutput
import com.listentoprabhupada.common.favorites_api.FavoritesOutput.*
import com.listentoprabhupada.common.favorites_impl.FavoritesComponentImpl
import com.listentoprabhupada.common.favorites_impl.FavoritesDeps
import com.listentoprabhupada.common.feature_favorites_api.FavoritesFeatureComponent
import com.listentoprabhupada.common.feature_favorites_api.FavoritesFeatureOutput
import com.listentoprabhupada.common.player_api.PlayerAction
import com.listentoprabhupada.common.player_api.PlayerComponent
import com.listentoprabhupada.common.player_impl.PlayerComponentImpl
import com.listentoprabhupada.common.utils.Consumer
import io.github.aakira.napier.Napier


class FavoritesFeatureComponentImpl(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    private val deps: FavoritesFeatureDeps,
    private val output: Consumer<FavoritesFeatureOutput>
) : FavoritesFeatureComponent, ComponentContext by componentContext {

    override val favoritesComponent: FavoritesComponent =
        FavoritesComponentImpl(
            componentContext = componentContext,
            storeFactory = storeFactory,
            deps = FavoritesDeps(deps.db, deps.api, deps.dispatchers),
            output = Consumer(::onFavoritesOutput)
        )

    override val playerComponent: PlayerComponent =
        PlayerComponentImpl(
            componentContext = componentContext,
            playerBus = deps.playerBus
        )

    init {
        deps.playerBus.observeState {
            favoritesComponent.onCurrentLecture(it.lecture.id, it.isPlaying)
        }
    }

    override fun onShowSettings() = output(FavoritesFeatureOutput.ShowSettings)

    private fun onFavoritesOutput(output: FavoritesOutput) =
        when(output) {
            Pause -> deps.playerBus.update(PlayerAction.Pause)
            is Play -> deps.playerBus.update(PlayerAction.Play(output.lectureId))
            is UpdatePlaylist -> deps.playerBus.update(output.lectures)
        }
}
