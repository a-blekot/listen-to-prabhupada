package com.listentoprabhupada.common.utils.billing

import com.listentoprabhupada.common.data.Donation
import com.listentoprabhupada.common.utils.billing.BillingEvent
import kotlinx.coroutines.flow.SharedFlow

interface BillingHelper {
    val availableDonations: List<Donation>
    val events: SharedFlow<BillingEvent>

    fun clean() {}
    fun purchase(donation: Donation) {}
    fun checkUnconsumedPurchases() {}
}