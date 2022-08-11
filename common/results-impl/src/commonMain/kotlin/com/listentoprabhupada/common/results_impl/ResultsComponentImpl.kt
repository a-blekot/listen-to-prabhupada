package com.listentoprabhupada.common.results_impl

import co.touchlab.stately.ensureNeverFrozen
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.doOnResume
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.listentoprabhupada.common.data.LectureOutput
import com.listentoprabhupada.common.results_api.ResultsComponent
import com.listentoprabhupada.common.results_api.ResultsState
import com.listentoprabhupada.common.results_impl.store.ResultsIntent.*
import com.listentoprabhupada.common.results_impl.store.ResultsStoreFactory
import com.listentoprabhupada.common.results_impl.store.ResultsLabel
import com.listentoprabhupada.common.utils.Consumer
import com.listentoprabhupada.common.utils.asValue
import com.listentoprabhupada.common.utils.getStore
import com.listentoprabhupada.common.utils.lifecycleCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import io.github.aakira.napier.Napier

class ResultsComponentImpl(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    private val deps: ResultsDeps,
    private val output: Consumer<LectureOutput>
) : ResultsComponent, ComponentContext by componentContext {

    private val store =
        instanceKeeper.getStore {
            ResultsStoreFactory(
                storeFactory = storeFactory,
                deps = deps,
            ).create()
        }

    private val scope: CoroutineScope = lifecycleCoroutineScope(deps.dispatchers.main)

    override val flow: Value<ResultsState> = store.asValue()

    init {
        store.labels
            .onEach(::handleLabel)
            .launchIn(scope)

        flow.ensureNeverFrozen()

        lifecycle.doOnResume {
            Napier.d( "LecturesComponent ON_RESUME")
            store.accept(UpdateLectures)
        }
    }

    override fun onPage(page: Int) = store.accept(UpdatePage(page))
    override fun onFavorite(id: Long, isFavorite: Boolean) = store.accept(Favorite(id = id, isFavorite = isFavorite))
    override fun onCurrentLecture(id: Long, isPlaying: Boolean) = store.accept(CurrentLecture(id, isPlaying))

    override fun onPause() = output(LectureOutput.Pause)
    override fun onPlay(id: Long) = output(LectureOutput.Play(id))

    private fun handleLabel(label: ResultsLabel) {
        when (label) {
            is ResultsLabel.LecturesLoaded -> {
                output(LectureOutput.UpdatePlaylist(label.lectures))
            }
        }
    }
}
