package com.prabhupadalectures.common.lectures_impl.mvi

import co.touchlab.stately.ensureNeverFrozen
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.LifecycleOwner
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.prabhupadalectures.common.lectures_api.LecturesComponent
import com.prabhupadalectures.common.lectures_api.LecturesOutput
import com.prabhupadalectures.common.lectures_api.LecturesState
import com.prabhupadalectures.common.lectures_impl.mvi.store.LecturesStore
import com.prabhupadalectures.common.lectures_impl.mvi.store.LecturesStore.Intent.*
import com.prabhupadalectures.common.lectures_impl.mvi.store.LecturesStoreFactory
import com.prabhupadalectures.common.utils.Consumer
import com.prabhupadalectures.common.utils.asValue
import com.prabhupadalectures.common.utils.getStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.coroutines.CoroutineContext

class LecturesComponentImpl(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    private val deps: LecturesDeps,
    private val output: Consumer<LecturesOutput>
) : LecturesComponent, ComponentContext by componentContext {

    private val store =
        instanceKeeper.getStore {
            LecturesStoreFactory(
                storeFactory = storeFactory,
                deps = deps,
            ).create()
        }

    private val scope: CoroutineScope = lifecycleCoroutineScope(deps.dispatchers.main)

    override val flow: Value<LecturesState> = store.asValue()

    init {
        store.labels
            .onEach(::handleLabel)
            .launchIn(scope)

        flow.ensureNeverFrozen()
    }

    override fun onPage(page: Int) = store.accept(UpdatePage(page))
    override fun onFavorite(id: Long, isFavorite: Boolean) = store.accept(Favorite(id = id, isFavorite = isFavorite))
    override fun onUpdateFilters() = store.accept(UpdateFilters)
    override fun onCurrentLecture(id: Long, isPlaying: Boolean) = store.accept(CurrentLecture(id, isPlaying))
    
    override fun onPause() = output(LecturesOutput.Pause)
    override fun onNext() = output(LecturesOutput.Next)
    override fun onPrev() = output(LecturesOutput.Prev)
    override fun onSeekForward() = output(LecturesOutput.SeekForward)
    override fun onSeekBack() = output(LecturesOutput.SeekBack)
    override fun onSliderReleased() = output(LecturesOutput.SliderReleased)
    override fun onPlay(id: Long) = output(LecturesOutput.Play(id))
    override fun onSeekTo(timeMs: Long) = output(LecturesOutput.SeekTo(timeMs))
    override fun onDownload(id: Long) =
        flow.value
            .lectures
            .firstOrNull { id == id }
            ?.let { output(LecturesOutput.Download(it)) }
            ?: Unit

    private fun handleLabel(label: LecturesStore.Label) {
        when (label) {
           is LecturesStore.Label.LecturesLoaded -> output(LecturesOutput.UpdatePlaylist(label.lectures))
        }
    }
}

fun LifecycleOwner.lifecycleCoroutineScope(coroutineContext: CoroutineContext = Dispatchers.Main): CoroutineScope {
    val scope = CoroutineScope(coroutineContext)
    lifecycle.doOnDestroy(scope::cancel)

    return scope
}