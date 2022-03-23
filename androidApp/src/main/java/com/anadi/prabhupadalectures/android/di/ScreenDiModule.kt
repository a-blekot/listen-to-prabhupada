package com.anadi.prabhupadalectures.android.di

import cafe.adriel.voyager.core.registry.ScreenProvider
import cafe.adriel.voyager.core.registry.screenModule
import com.anadi.prabhupadalectures.android.navigation.ScreenModule
import com.anadi.prabhupadalectures.android.ui.screens.downloads.DownloadsScreen
import com.anadi.prabhupadalectures.android.ui.screens.favorites.FavoritesScreen
import com.anadi.prabhupadalectures.android.ui.screens.results.ResultsScreen
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

sealed class Route : ScreenProvider {
    object RMainScreen : Route()
    object Favorites : Route()
    object Downloads : Route()
}

@Module
@InstallIn(SingletonComponent::class)
object ScreenDiModule {
    @Provides
    @IntoSet
    fun provideScreenModule(): ScreenModule {
        return screenModule {
            register<Route.RMainScreen> {
                ResultsScreen()
            }
            register<Route.Favorites> {
                FavoritesScreen()
            }
            register<Route.Downloads> {
                DownloadsScreen()
            }
        }
    }
}