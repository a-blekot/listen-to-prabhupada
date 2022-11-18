package com.listentoprabhupada.common.root

import com.listentoprabhupada.common.data.VersionInfo
import com.listentoprabhupada.common.database.Database
import com.listentoprabhupada.common.network_api.PrabhupadaApi
import com.listentoprabhupada.common.player_api.PlayerBus
import com.listentoprabhupada.common.utils.RemoteConfig
import com.listentoprabhupada.common.utils.analytics.Analytics
import com.listentoprabhupada.common.utils.billing.BillingHelper
import com.listentoprabhupada.common.utils.connectivity.ConnectivityObserver
import com.listentoprabhupada.common.utils.dispatchers.DispatcherProvider
import com.listentoprabhupada.common.utils.resources.StringResourceHandler
import kotlin.native.concurrent.SharedImmutable

data class RootDeps(
    val db: Database,
    val api: PrabhupadaApi,
    val playerBus: PlayerBus,
    val remoteConfig: RemoteConfig,
    val dispatchers: DispatcherProvider,
    val connectivityObserver: ConnectivityObserver,
    val stringResourceHandler: StringResourceHandler,
    val billingHelper: BillingHelper?,
    val analytics: Analytics,
    val versionInfo: VersionInfo,
    val onEmail: () -> Unit,
    val onRateUs: () -> Unit,
    val onShareApp: () -> Unit,
    val onInappReview: () -> Unit
)