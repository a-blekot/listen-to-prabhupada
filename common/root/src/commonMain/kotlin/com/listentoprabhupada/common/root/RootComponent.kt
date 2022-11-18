package com.listentoprabhupada.common.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.listentoprabhupada.common.donations_api.DonationsComponent
import com.listentoprabhupada.common.downloads_api.DownloadsComponent
import com.listentoprabhupada.common.favorites_api.FavoritesComponent
import com.listentoprabhupada.common.filters_api.FiltersComponent
import com.listentoprabhupada.common.player_api.PlayerComponent
import com.listentoprabhupada.common.results_api.ResultsComponent
import com.listentoprabhupada.common.settings_api.SettingsComponent

interface RootComponent {

    val childStack: Value<ChildStack<*, Child>>
    val playerComponent: PlayerComponent
    val settingsComponent: SettingsComponent

    fun onResultsTabClicked() {}
    fun onFavoritesTabClicked() {}
    fun onDownloadsTabClicked() {}
    fun onFiltersTabClicked() {}
    fun onSearchTabClicked() {}
    fun onSettingsTabClicked() {}

    sealed interface Child {
        data class Results(val component: ResultsComponent) : Child
        data class Favorites(val component: FavoritesComponent) : Child
        data class Downloads(val component: DownloadsComponent) : Child
        data class Filters(val component: FiltersComponent) : Child
        data class Settings(val component: SettingsComponent) : Child
        data class Donations(val component: DonationsComponent) : Child
    }
}
