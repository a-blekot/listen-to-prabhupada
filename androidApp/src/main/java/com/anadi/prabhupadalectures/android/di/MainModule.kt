package com.anadi.prabhupadalectures.android.di

import android.content.Context
import com.anadi.prabhupadalectures.android.BuildConfig
import com.anadi.prabhupadalectures.android.PrabhupadaApp.Companion.app
import com.anadi.prabhupadalectures.data.Database
import com.anadi.prabhupadalectures.data.DatabaseDriverFactory
import com.anadi.prabhupadalectures.data.DatabaseImpl
import com.anadi.prabhupadalectures.network.api.PrabhupadaApi
import com.anadi.prabhupadalectures.network.api.createPrabhupadaApi
import com.anadi.prabhupadalectures.repository.*
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MainModule {

    @[Provides Singleton]
    fun provideContext(): Context = app

    @[Provides Singleton]
    fun provideBackgroundScope(): CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.Default)

    @[Provides Singleton]
    fun provideDatabase(@ApplicationContext appContext: Context): Database =
        DatabaseImpl(DatabaseDriverFactory(appContext))

    @[Provides Singleton]
    fun provideApi(): PrabhupadaApi =
        createPrabhupadaApi()

    @[Provides Singleton]
    fun provideResultsRepository(
        db: Database,
        api: PrabhupadaApi,
        playbackRepository: PlaybackRepository
    ): ResultsRepository =
        ResultsRepositoryImpl(db, api, playbackRepository, BuildConfig.DEBUG)

    @[Provides Singleton]
    fun provideDownloadsRepository(db: Database, api: PrabhupadaApi): DownloadsRepository =
        DownloadsRepositoryImpl(db, api)

    @[Provides Singleton]
    fun providePlaybackRepository(): PlaybackRepository =
        PlaybackRepositoryImpl()

    @[Provides Singleton]
    fun provideToolsRepository(db: Database): ToolsRepository =
        ToolsRepositoryImpl(db)
}
