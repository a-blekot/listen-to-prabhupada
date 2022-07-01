package com.prabhupadalectures.common.feature_results_impl

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.prabhupadalectures.common.feature_results_api.ResultsComponent
import com.prabhupadalectures.common.feature_results_api.ResultsOutput
import com.prabhupadalectures.common.lectures_api.LecturesComponent
import com.prabhupadalectures.common.lectures_api.LecturesOutput
import com.prabhupadalectures.common.lectures_api.LecturesOutput.*
import com.prabhupadalectures.common.lectures_impl.mvi.LecturesComponentImpl
import com.prabhupadalectures.common.lectures_impl.mvi.LecturesDeps
import com.prabhupadalectures.common.player_api.PlayerAction
import com.prabhupadalectures.common.player_api.PlayerComponent
import com.prabhupadalectures.common.player_impl.PlayerComponentImpl
import com.prabhupadalectures.common.utils.Consumer


class ResultsComponentImpl(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    private val deps: ResultsDeps,
    private val output: Consumer<ResultsOutput>,
) : ResultsComponent, ComponentContext by componentContext {

    override val lecturesComponent: LecturesComponent =
        LecturesComponentImpl(
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
            lecturesComponent.onCurrentLecture(it.lecture.id, it.isPlaying)
        }
    }

    override fun onUpdateFilters() = lecturesComponent.onUpdateFilters()
    override fun onEditFilters() = output(ResultsOutput.EditFilters)
    override fun onShowDownloads() = output(ResultsOutput.ShowDownloads)
    override fun onShowFavorites() = output(ResultsOutput.ShowFavorites)
    override fun onShowSettings() = output(ResultsOutput.ShowSettings)

    private fun onLecturesOutput(output: LecturesOutput) =
        when(output) {
            Pause -> deps.playerBus.update(PlayerAction.Pause)
            is Play -> deps.playerBus.update(PlayerAction.Play(output.lectureId))
            is Download -> deps.playerBus.update(PlayerAction.Download(output.lecture))
            is UpdatePlaylist -> deps.playerBus.update(output.lectures)
        }
}
