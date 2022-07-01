package com.prabhupadalectures.common.root

import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.value.Value
import com.prabhupadalectures.common.feature_favorites_api.FavoritesFeatureComponent
import com.prabhupadalectures.common.feature_downloads_api.DownloadsFeatureComponent
import com.prabhupadalectures.common.feature_results_api.ResultsComponent
import com.prabhupadalectures.common.filters.FiltersComponent

interface RootComponent {

    val routerState: Value<RouterState<*, Child>>

    sealed interface Child {
        data class Results(val component: ResultsComponent) : Child
        data class Favorites(val component: FavoritesFeatureComponent) : Child
        data class Downloads(val component: DownloadsFeatureComponent) : Child
        data class Filters(val component: FiltersComponent) : Child
    }
}
