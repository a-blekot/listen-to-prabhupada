package com.listentoprabhupada.common.feature_results_impl

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.listentoprabhupada.common.feature_results_api.ResultsComponent
import com.listentoprabhupada.common.feature_results_api.ResultsOutput
import com.listentoprabhupada.common.results_api.ResultsComponent
import com.listentoprabhupada.common.results_api.ResultsOutput
import com.listentoprabhupada.common.results_api.ResultsOutput.*
import com.listentoprabhupada.common.results_impl.ResultsComponentImpl
import com.listentoprabhupada.common.results_impl.LecturesDeps
import com.listentoprabhupada.common.player_api.PlayerAction
import com.listentoprabhupada.common.player_api.PlayerComponent
import com.listentoprabhupada.common.player_impl.PlayerComponentImpl
import com.listentoprabhupada.common.utils.Consumer


class ResultsComponentImpl(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    private val deps: ResultsDeps,
    private val output: Consumer<ResultsOutput>,
) : ResultsComponent, ComponentContext by componentContext {

    val resultsComponent: com.listentoprabhupada.common.results_api.ResultsComponent =
        ResultsComponentImpl(
            componentContext = componentContext,
            storeFactory = storeFactory,
            deps = LecturesDeps(deps.db, deps.api, deps.dispatchers),
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

    override fun onEditFilters() = output(ResultsOutput.EditFilters)
    override fun onShowDownloads() = output(ResultsOutput.ShowDownloads)
    override fun onShowFavorites() = output(ResultsOutput.ShowFavorites)
    override fun onShowSettings() = output(ResultsOutput.ShowSettings)

    private fun onLecturesOutput(output: com.listentoprabhupada.common.results_api.ResultsOutput) =
        when(output) {
            Pause -> deps.playerBus.update(PlayerAction.Pause)
            is Play -> deps.playerBus.update(PlayerAction.Play(output.lectureId))
            is UpdatePlaylist -> deps.playerBus.update(output.lectures)
        }
}
