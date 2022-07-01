package com.prabhupadalectures.common.downloads_impl


import co.touchlab.stately.ensureNeverFrozen
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.prabhupadalectures.common.downloads_api.DownloadsComponent
import com.prabhupadalectures.common.downloads_api.DownloadsOutput
import com.prabhupadalectures.common.downloads_api.DownloadsState
import com.prabhupadalectures.common.downloads_impl.store.DownloadsIntent.*
import com.prabhupadalectures.common.downloads_impl.store.DownloadsLabel
import com.prabhupadalectures.common.downloads_impl.store.DownloadsStoreFactory
import com.prabhupadalectures.common.utils.Consumer
import com.prabhupadalectures.common.utils.asValue
import com.prabhupadalectures.common.utils.getStore
import com.prabhupadalectures.common.utils.lifecycleCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.coroutines.CoroutineContext
import io.github.aakira.napier.Napier


class DownloadsComponentImpl(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    deps: DownloadsDeps,
    private val output: Consumer<DownloadsOutput>
) : DownloadsComponent, ComponentContext by componentContext {

    private val store =
        instanceKeeper.getStore {
            DownloadsStoreFactory(
                storeFactory = storeFactory,
                deps = deps,
            ).create()
        }

    private val scope: CoroutineScope = lifecycleCoroutineScope(deps.dispatchers.main)

    override val flow: Value<DownloadsState> = store.asValue()

    init {
        store.labels
            .onEach(::handleLabel)
            .launchIn(scope)

        flow.ensureNeverFrozen()

        store.init()
    }

    override fun onPlay(id: Long) = output(DownloadsOutput.Play(id))
    override fun onPause() = output(DownloadsOutput.Pause)
    override fun onFavorite(id: Long, isFavorite: Boolean) = store.accept(Favorite(id = id, isFavorite = isFavorite))
    override fun onCurrentLecture(id: Long, isPlaying: Boolean) = store.accept(CurrentLecture(id, isPlaying))

    private fun handleLabel(label: DownloadsLabel) {
        when (label) {
            is DownloadsLabel.LecturesLoaded -> output(DownloadsOutput.UpdatePlaylist(label.lectures))
        }
    }
}
