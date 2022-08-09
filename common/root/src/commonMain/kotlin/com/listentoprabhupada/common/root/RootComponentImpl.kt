package com.listentoprabhupada.common.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.*
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.listentoprabhupada.common.feature_downloads_api.DownloadsFeatureComponent
import com.listentoprabhupada.common.feature_downloads_api.DownloadsFeatureOutput
import com.listentoprabhupada.common.feature_downloads_impl.DownloadsFeatureComponentImpl
import com.listentoprabhupada.common.feature_downloads_impl.DownloadsFeatureDeps
import com.listentoprabhupada.common.feature_favorites_api.FavoritesFeatureComponent
import com.listentoprabhupada.common.feature_favorites_api.FavoritesFeatureOutput
import com.listentoprabhupada.common.feature_favorites_impl.FavoritesFeatureComponentImpl
import com.listentoprabhupada.common.feature_favorites_impl.FavoritesFeatureDeps
import com.listentoprabhupada.common.feature_results_api.ResultsComponent
import com.listentoprabhupada.common.feature_results_api.ResultsOutput
import com.listentoprabhupada.common.feature_results_impl.ResultsComponentImpl
import com.listentoprabhupada.common.feature_results_impl.ResultsDeps
import com.listentoprabhupada.common.filters_api.FiltersComponent
import com.listentoprabhupada.common.filters_api.FiltersOutput
import com.listentoprabhupada.common.filters_impl.FiltersDeps
import com.listentoprabhupada.common.filters_impl.FiltersComponentImpl
import com.listentoprabhupada.common.root.RootComponent.Child.*
import com.listentoprabhupada.common.utils.Consumer

class RootComponentImpl internal constructor(
    componentContext: ComponentContext,
    private val results: (ComponentContext, Consumer<ResultsOutput>) -> ResultsComponent,
    private val favorites: (ComponentContext, Consumer<FavoritesFeatureOutput>) -> FavoritesFeatureComponent,
    private val downloads: (ComponentContext, Consumer<DownloadsFeatureOutput>) -> DownloadsFeatureComponent,
    private val filters: (ComponentContext, Consumer<FiltersOutput>) -> FiltersComponent
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
        downloads = { childContext, output ->
            DownloadsFeatureComponentImpl(
                componentContext = childContext,
                storeFactory = storeFactory,
                deps = deps.run { DownloadsFeatureDeps(db, api, playerBus, dispatchers) },
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

    override fun onResultsTabClicked() =
        router.bringToFront(Configuration.Results)

    override fun onFavoritesTabClicked() =
        router.bringToFront(Configuration.Favorites)

    override fun onDownloadsTabClicked() =
        router.bringToFront(Configuration.Downloads)

    override fun onFiltersTabClicked() =
        router.bringToFront(Configuration.Filters)

    private fun createChild(configuration: Configuration, componentContext: ComponentContext): RootComponent.Child =
        when (configuration) {
            is Configuration.Results -> Results(results(componentContext, Consumer(::onResultsOutput)))
            is Configuration.Favorites -> Favorites(favorites(componentContext, Consumer(::onFavoritesOutput)))
            is Configuration.Downloads -> Downloads(downloads(componentContext, Consumer(::onDownloadsOutput)))
            is Configuration.Filters -> Filters(filters(componentContext, Consumer(::onFiltersOutput)))
        }

    private fun onResultsOutput(output: ResultsOutput): Unit =
        when (output) {
            is ResultsOutput.EditFilters -> router.push(Configuration.Filters)
            is ResultsOutput.ShowFavorites -> router.push(Configuration.Favorites)
            is ResultsOutput.ShowDownloads -> router.push(Configuration.Downloads)
            else -> { /** TODO **/}
        }

    private fun onFavoritesOutput(output: FavoritesFeatureOutput): Unit =
        when (output) {
//            is FavoritesFeatureOutput.ShowSettings -> router.push(Configuration.Filters)
            else -> { /** TODO **/}
        }

    private fun onDownloadsOutput(output: DownloadsFeatureOutput): Unit =
        when (output) {
//            is FavoritesFeatureOutput.ShowSettings -> router.push(Configuration.Filters)
            else -> { /** TODO **/}
        }

    private fun onFiltersOutput(output: FiltersOutput): Unit =
        when (output) {
            is FiltersOutput.ShowResults -> router.pop { isSuccess ->
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
        object Downloads : Configuration()

        @Parcelize
        object Filters : Configuration()
    }
}



