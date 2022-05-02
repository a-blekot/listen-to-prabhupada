package com.prabhupadalectures.lectures.mvi.lectures

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.prabhupadalectures.common.utils.asValue
import com.prabhupadalectures.common.utils.getStore
import com.prabhupadalectures.lectures.data.QueryParam
import com.prabhupadalectures.lectures.mvi.Dependencies
import com.prabhupadalectures.lectures.mvi.lectures.Results.Model
import com.prabhupadalectures.lectures.mvi.lectures.store.ResultsStore.Intent.*
import com.prabhupadalectures.lectures.mvi.lectures.store.ResultsStore.State
import com.prabhupadalectures.lectures.mvi.lectures.store.ResultsStoreFactory

class ResultsComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    deps: Dependencies,
) : Results, ComponentContext by componentContext {

    private val store =
        instanceKeeper.getStore {
            ResultsStoreFactory(
                storeFactory = storeFactory,
                deps = deps,
            ).create()
        }

    override val models: Value<Model> = store.asValue().map(stateToModel)

    override fun onLoadPrev() = store.accept(LoadPrev)
    override fun onLoadNext() = store.accept(LoadNext)
    override fun onPage(page: Int) = store.accept(UpdatePage(page))
    override fun onQueryParam(queryParam: QueryParam) = store.accept(UpdateQuery(queryParam))
    override fun onPause() = store.accept(Pause)
    override fun onPlay(id: Long) = store.accept(Play(id = id))
    override fun onDownload(id: Long) = store.accept(Download(id = id))
    override fun onFavorite(id: Long, isFavorite: Boolean) = store.accept(Favorite(id = id, isFavorite = isFavorite))
}

internal val stateToModel: (State) -> Model =
    { state ->
        Model(
            state.isLoading,
            state.lectures,
            state.filters,
            state.pagination
        )
    }