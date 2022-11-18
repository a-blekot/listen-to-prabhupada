package com.listentoprabhupada.common.donations_impl

import com.listentoprabhupada.common.utils.analytics.Analytics
import com.listentoprabhupada.common.utils.billing.BillingHelper
import com.listentoprabhupada.common.utils.connectivity.ConnectivityObserver
import com.listentoprabhupada.common.utils.dispatchers.DispatcherProvider
import com.listentoprabhupada.common.utils.resources.StringResourceHandler

data class DonationsDeps(
    val analytics: Analytics,
    val billingHelper: BillingHelper?,
    val dispatchers: DispatcherProvider,
    val connectivityObserver: ConnectivityObserver,
    val stringResourceHandler: StringResourceHandler
)
