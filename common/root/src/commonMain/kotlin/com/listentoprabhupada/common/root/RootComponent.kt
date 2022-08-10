package com.listentoprabhupada.common.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.listentoprabhupada.common.feature_favorites_api.FavoritesFeatureComponent
import com.listentoprabhupada.common.feature_downloads_api.DownloadsFeatureComponent
import com.listentoprabhupada.common.feature_results_api.ResultsFeatureComponent
import com.listentoprabhupada.common.filters_api.FiltersComponent
import com.listentoprabhupada.common.settings_api.SettingsComponent

interface RootComponent {

    val childStack: Value<ChildStack<*, Child>>

    fun onResultsTabClicked() {}
    fun onFavoritesTabClicked() {}
    fun onDownloadsTabClicked() {}
    fun onFiltersTabClicked() {}
    fun onSearchTabClicked() {}
    fun onSettingsTabClicked() {}

    sealed interface Child {
        data class Results(val component: ResultsFeatureComponent) : Child
        data class Favorites(val component: FavoritesFeatureComponent) : Child
        data class Downloads(val component: DownloadsFeatureComponent) : Child
        data class Filters(val component: FiltersComponent) : Child
//        data class Search(val component: SearchComponent) : Child
        data class Settings(val component: SettingsComponent) : Child
    }
}
