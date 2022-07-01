package com.prabhupadalectures.common.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.*
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.prabhupadalectures.common.feature_favorites_api.FavoritesFeatureComponent
import com.prabhupadalectures.common.feature_favorites_api.FavoritesFeatureOutput
import com.prabhupadalectures.common.feature_favorites_impl.FavoritesFeatureComponentImpl
import com.prabhupadalectures.common.feature_favorites_impl.FavoritesFeatureDeps
import com.prabhupadalectures.common.feature_results_api.ResultsComponent
import com.prabhupadalectures.common.feature_results_api.ResultsOutput
import com.prabhupadalectures.common.feature_results_impl.ResultsComponentImpl
import com.prabhupadalectures.common.feature_results_impl.ResultsDeps
import com.prabhupadalectures.common.filters.FiltersDeps
import com.prabhupadalectures.common.filters.FiltersComponent
import com.prabhupadalectures.common.filters.FiltersComponentImpl
import com.prabhupadalectures.common.root.RootComponent.Child.*
import com.prabhupadalectures.common.utils.Consumer

class RootComponentImpl internal constructor(
    componentContext: ComponentContext,
    private val results: (ComponentContext, Consumer<ResultsOutput>) -> ResultsComponent,
    private val favorites: (ComponentContext, Consumer<FavoritesFeatureOutput>) -> FavoritesFeatureComponent,
    private val filters: (ComponentContext, Consumer<FiltersComponent.Output>) -> FiltersComponent
) : RootComponent, ComponentContext by componentContext {

    constructor(
        componentContext: ComponentContext,
        storeFactory: StoreFactory,
        deps: RootDeps,
    ) : this(
        componentContext = componentContext,
        results = { childContext, output ->
            ResultsComponentImpl(
                componentContext = childContext,
                storeFactory = storeFactory,
                deps = deps.run { ResultsDeps(db, api, playerBus, dispatchers) },
                output = output
            )
        },
        favorites = { childContext, output ->
            FavoritesFeatureComponentImpl(
                componentContext = childContext,
                storeFactory = storeFactory,
                deps = deps.run { FavoritesFeatureDeps(db, api, playerBus, dispatchers) },
                output = output
            )
        },
        filters = { childContext, output ->
            FiltersComponentImpl(
                componentContext = childContext,
                storeFactory = storeFactory,
                deps = deps.run { FiltersDeps(db, api, dispatchers) },
                output = output
            )
        }
    )

    private val router =
        router<Configuration, RootComponent.Child>(
            initialConfiguration = Configuration.Results,
            handleBackButton = true,
            childFactory = ::createChild
        )

    override val routerState: Value<RouterState<*, RootComponent.Child>> = router.state

    private fun createChild(configuration: Configuration, componentContext: ComponentContext): RootComponent.Child =
        when (configuration) {
            is Configuration.Results -> Results(results(componentContext, Consumer(::onResultsOutput)))
            is Configuration.Favorites -> Favorites(favorites(componentContext, Consumer(::onFavoritesOutput)))
            is Configuration.Filters -> Filters(filters(componentContext, Consumer(::onFiltersOutput)))
        }

    private fun onResultsOutput(output: ResultsOutput): Unit =
        when (output) {
            is ResultsOutput.EditFilters -> router.push(Configuration.Filters)
            is ResultsOutput.ShowFavorites -> router.push(Configuration.Favorites)
            else -> { /** TODO **/}
        }

    private fun onFavoritesOutput(output: FavoritesFeatureOutput): Unit =
        when (output) {
//            is FavoritesFeatureOutput.ShowSettings -> router.push(Configuration.Filters)
            else -> { /** TODO **/}
        }

    private fun onFiltersOutput(output: FiltersComponent.Output): Unit =
        when (output) {
            is FiltersComponent.Output.ShowResults -> router.pop { isSuccess ->
//                if (isSuccess) {
//                    (router.activeChild.instance as? Results)?.component?.onUpdateFilters()
//                }
            }
        }

    private sealed class Configuration : Parcelable {
        @Parcelize
        object Results : Configuration()

        @Parcelize
        object Favorites : Configuration()

        @Parcelize
        object Filters : Configuration()
    }
}



