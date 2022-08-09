package com.listentoprabhupada.common.root

import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.value.Value
import com.listentoprabhupada.common.feature_favorites_api.FavoritesFeatureComponent
import com.listentoprabhupada.common.feature_downloads_api.DownloadsFeatureComponent
import com.listentoprabhupada.common.feature_results_api.ResultsComponent
import com.listentoprabhupada.common.filters_api.FiltersComponent

interface RootComponent {

    val routerState: Value<RouterState<*, Child>>

    fun onResultsTabClicked() {}
    fun onFavoritesTabClicked() {}
    fun onDownloadsTabClicked() {}
    fun onFiltersTabClicked() {}

    sealed interface Child {
        data class Results(val component: ResultsComponent) : Child
        data class Favorites(val component: FavoritesFeatureComponent) : Child
        data class Downloads(val component: DownloadsFeatureComponent) : Child
        data class Filters(val component: FiltersComponent) : Child
    }
}
