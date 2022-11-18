package com.listentoprabhupada.common.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.listentoprabhupada.common.data.LectureOutput
import com.listentoprabhupada.common.donations_api.DonationsComponent
import com.listentoprabhupada.common.donations_api.DonationsOutput
import com.listentoprabhupada.common.donations_impl.DonationsComponentImpl
import com.listentoprabhupada.common.donations_impl.DonationsDeps
import com.listentoprabhupada.common.downloads_api.DownloadsComponent
import com.listentoprabhupada.common.downloads_impl.DownloadsComponentImpl
import com.listentoprabhupada.common.downloads_impl.DownloadsDeps
import com.listentoprabhupada.common.favorites_api.FavoritesComponent
import com.listentoprabhupada.common.favorites_impl.FavoritesComponentImpl
import com.listentoprabhupada.common.favorites_impl.FavoritesDeps
import com.listentoprabhupada.common.filters_api.FiltersComponent
import com.listentoprabhupada.common.filters_api.FiltersOutput
import com.listentoprabhupada.common.filters_impl.FiltersComponentImpl
import com.listentoprabhupada.common.filters_impl.FiltersDeps
import com.listentoprabhupada.common.player_api.PlayerAction
import com.listentoprabhupada.common.player_api.PlayerState
import com.listentoprabhupada.common.player_impl.PlayerComponentImpl
import com.listentoprabhupada.common.results_api.ResultsComponent
import com.listentoprabhupada.common.results_impl.ResultsComponentImpl
import com.listentoprabhupada.common.results_impl.ResultsDeps
import com.listentoprabhupada.common.root.RootComponent.Child.*
import com.listentoprabhupada.common.settings_api.SettingsComponent
import com.listentoprabhupada.common.settings_api.SettingsOutput
import com.listentoprabhupada.common.settings_impl.SettingsComponentImpl
import com.listentoprabhupada.common.settings_impl.SettingsDeps
import com.listentoprabhupada.common.utils.Consumer

class RootComponentImpl internal constructor(
    storeFactory: StoreFactory,
    componentContext: ComponentContext,
    private val deps: RootDeps,
    private val results: (ComponentContext, Consumer<LectureOutput>) -> ResultsComponent,
    private val favorites: (ComponentContext, Consumer<LectureOutput>) -> FavoritesComponent,
    private val downloads: (ComponentContext, Consumer<LectureOutput>) -> DownloadsComponent,
    private val filters: (ComponentContext, Consumer<FiltersOutput>) -> FiltersComponent,
    private val settings: (ComponentContext, Consumer<SettingsOutput>) -> SettingsComponent,
    private val donations: (ComponentContext, Consumer<DonationsOutput>) -> DonationsComponent,
) : RootComponent, ComponentContext by componentContext {

    constructor(
        componentContext: ComponentContext,
        storeFactory: StoreFactory,
        deps: RootDeps,
    ) : this(
        storeFactory = storeFactory,
        componentContext = componentContext,
        deps = deps,
        results = { childContext, output ->
            ResultsComponentImpl(
                componentContext = childContext,
                storeFactory = storeFactory,
                deps = deps.run { ResultsDeps(db, api, remoteConfig, dispatchers) },
                output = output
            )
        },
        favorites = { childContext, output ->
            FavoritesComponentImpl(
                componentContext = childContext,
                storeFactory = storeFactory,
                deps = deps.run { FavoritesDeps(db, api, dispatchers) },
                output = output
            )
        },
        downloads = { childContext, output ->
            DownloadsComponentImpl(
                componentContext = childContext,
                storeFactory = storeFactory,
                deps = deps.run { DownloadsDeps(db, api, dispatchers) },
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
        },
        settings = { childContext, output ->
            SettingsComponentImpl(
                componentContext = childContext,
                storeFactory = storeFactory,
                deps = deps.run { SettingsDeps(analytics, dispatchers, versionInfo) },
                output = output
            )
        },
        donations = { childContext, output ->
            DonationsComponentImpl(
                componentContext = childContext,
                storeFactory = storeFactory,
                deps = deps.run { DonationsDeps(analytics, billingHelper, dispatchers, connectivityObserver, stringResourceHandler) },
                output = output
            )
        },
    )

    private val navigation = StackNavigation<Configuration>()

    private val stack =
        childStack(
            source = navigation,
            initialConfiguration = Configuration.Results,
            handleBackButton = true,
            childFactory = ::createChild
        )

    override val childStack: Value<ChildStack<*, RootComponent.Child>>
        get() = stack

    override val playerComponent=
        PlayerComponentImpl(
            componentContext = componentContext,
            playerBus = deps.playerBus
        )

    override val settingsComponent =
        SettingsComponentImpl(
            componentContext = componentContext,
            storeFactory = storeFactory,
            deps = deps.run { SettingsDeps(analytics, dispatchers, versionInfo) },
            output = Consumer(::onSettingsOutput)
        )

    init {
        deps.playerBus.observeState(::onPlayerStateChanged)
    }

    private fun onPlayerStateChanged(state: PlayerState) {
        when (val child = childStack.value.active.instance) {
            is Results -> child.component.onCurrentLecture(state.lecture.id, state.isPlaying)
            is Favorites -> child.component.onCurrentLecture(state.lecture.id, state.isPlaying)
            is Downloads -> child.component.onCurrentLecture(state.lecture.id, state.isPlaying)
            else -> { /** do nothing **/ }
        }
    }

    override fun onResultsTabClicked() =
        navigation.bringToFront(Configuration.Results)

    override fun onFavoritesTabClicked() =
        navigation.bringToFront(Configuration.Favorites)

    override fun onDownloadsTabClicked() =
        navigation.bringToFront(Configuration.Downloads)

    override fun onFiltersTabClicked() =
        navigation.bringToFront(Configuration.Filters)

    private fun createChild(
        configuration: Configuration,
        componentContext: ComponentContext
    ): RootComponent.Child =
        when (configuration) {
            is Configuration.Results ->
                Results(results(componentContext, Consumer(::onLecturesOutput)))
            is Configuration.Favorites ->
                Favorites(favorites(componentContext, Consumer(::onLecturesOutput)))
            is Configuration.Downloads ->
                Downloads(downloads(componentContext, Consumer(::onLecturesOutput)))
            is Configuration.Filters ->
                Filters(filters(componentContext, Consumer(::onFiltersOutput)))
            is Configuration.Settings ->
                Settings(settings(componentContext, Consumer(::onSettingsOutput)))
            is Configuration.Donations ->
                Donations(donations(componentContext, Consumer(::onDonationsOutput)))
        }

    private fun onLecturesOutput(output: LectureOutput) =
        when (output) {
            LectureOutput.Pause -> deps.playerBus.update(PlayerAction.Pause)
            is LectureOutput.Play -> deps.playerBus.update(PlayerAction.Play(output.lectureId))
            is LectureOutput.UpdatePlaylist -> deps.playerBus.update(output.lectures)
            else -> { /** should be handled in results, favorites, downloads**/ }
        }

    private fun onFiltersOutput(output: FiltersOutput): Unit =
        when (output) {
            is FiltersOutput.ShowResults -> navigation.bringToFront(Configuration.Results)
        }

    private fun onSettingsOutput(output: SettingsOutput) {
        when (output) {
            SettingsOutput.Email -> deps.onEmail()
            SettingsOutput.ShareApp -> deps.onShareApp()
            SettingsOutput.RateUs -> deps.onRateUs()
            SettingsOutput.Donations -> navigation.push(Configuration.Donations)
            SettingsOutput.Back -> navigation.pop()
        }
    }

    private fun onDonationsOutput(output: DonationsOutput) {
        when (output) {
            DonationsOutput.SuccessPurchase -> navigation.pop()
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

        @Parcelize
        object Settings : Configuration()

        @Parcelize
        object Donations : Configuration()
    }
}



