package com.listentoprabhupada.common.utils.billing

import com.listentoprabhupada.common.data.Donation

sealed interface BillingEvent {

    data class PurchaseSuccess(val donation: Donation?) : BillingEvent
    data class DonationsLoaded(val list: List<Donation>) : BillingEvent

    data class Error(
        val operation: BillingOperation,
        val responseCode: Int = 0,
        val msg: String = "",
        val throwable: Throwable? = null
    ) : BillingEvent
}

