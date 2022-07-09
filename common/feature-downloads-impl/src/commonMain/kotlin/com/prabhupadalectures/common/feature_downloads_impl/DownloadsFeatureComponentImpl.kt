package com.prabhupadalectures.common.feature_downloads_impl

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.prabhupadalectures.common.downloads_api.DownloadsComponent
import com.prabhupadalectures.common.downloads_api.DownloadsOutput
import com.prabhupadalectures.common.downloads_api.DownloadsOutput.*
import com.prabhupadalectures.common.downloads_impl.DownloadsComponentImpl
import com.prabhupadalectures.common.downloads_impl.DownloadsDeps
import com.prabhupadalectures.common.feature_downloads_api.DownloadsFeatureComponent
import com.prabhupadalectures.common.feature_downloads_api.DownloadsFeatureOutput
import com.prabhupadalectures.common.player_api.PlayerAction
import com.prabhupadalectures.common.player_api.PlayerComponent
import com.prabhupadalectures.common.player_impl.PlayerComponentImpl
import com.prabhupadalectures.common.utils.Consumer
import io.github.aakira.napier.Napier


class DownloadsFeatureComponentImpl(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    private val deps: DownloadsFeatureDeps,
    private val output: Consumer<DownloadsFeatureOutput>
) : DownloadsFeatureComponent, ComponentContext by componentContext {

    override val downloadsComponent: DownloadsComponent =
        DownloadsComponentImpl(
            componentContext = componentContext,
            storeFactory = storeFactory,
            deps = DownloadsDeps(deps.db, deps.api, deps.dispatchers),
            output = Consumer(::onDownloadsOutput)
        )

    override val playerComponent: PlayerComponent =
        PlayerComponentImpl(
            componentContext = componentContext,
            playerBus = deps.playerBus
        )

    init {
        deps.playerBus.observeState {
            downloadsComponent.onCurrentLecture(it.lecture.id, it.isPlaying)
        }
    }

    override fun onShowSettings() = output(DownloadsFeatureOutput.ShowSettings)

    private fun onDownloadsOutput(output: DownloadsOutput) =
        when(output) {
            Pause -> deps.playerBus.update(PlayerAction.Pause)
            is Play -> deps.playerBus.update(PlayerAction.Play(output.lectureId))
            is UpdatePlaylist -> deps.playerBus.update(output.lectures)
        }
}
