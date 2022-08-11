package com.listentoprabhupada.common.feature_results_impl

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.listentoprabhupada.common.feature_results_api.ResultsFeatureComponent
import com.listentoprabhupada.common.feature_results_api.ResultsFeatureOutput
import com.listentoprabhupada.common.results_api.ResultsComponent
import com.listentoprabhupada.common.results_api.ResultsOutput
import com.listentoprabhupada.common.results_api.ResultsOutput.*
import com.listentoprabhupada.common.results_impl.ResultsComponentImpl
import com.listentoprabhupada.common.results_impl.ResultsDeps
import com.listentoprabhupada.common.player_api.PlayerAction
import com.listentoprabhupada.common.player_api.PlayerComponent
import com.listentoprabhupada.common.player_impl.PlayerComponentImpl
import com.listentoprabhupada.common.utils.Consumer


class ResultsFeatureComponentImpl(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    private val deps: ResultsFeatureDeps,
    private val output: Consumer<ResultsFeatureOutput>,
) : ResultsFeatureComponent, ComponentContext by componentContext {

    override val resultsComponent: ResultsComponent =
        ResultsComponentImpl(
            componentContext = componentContext,
            storeFactory = storeFactory,
            deps = ResultsDeps(deps.db, deps.api, deps.remoteConfig, deps.dispatchers),
            output = Consumer(::onLecturesOutput)
        )

    override val playerComponent: PlayerComponent =
        PlayerComponentImpl(
            componentContext = componentContext,
            playerBus = deps.playerBus
        )

    init {
        deps.playerBus.observeState {
            resultsComponent.onCurrentLecture(it.lecture.id, it.isPlaying)
        }
    }

    private fun onLecturesOutput(output: ResultsOutput) =
        when(output) {
            Pause -> deps.playerBus.update(PlayerAction.Pause)
            is Play -> deps.playerBus.update(PlayerAction.Play(output.lectureId))
            is UpdatePlaylist -> deps.playerBus.update(output.lectures)
        }
}
