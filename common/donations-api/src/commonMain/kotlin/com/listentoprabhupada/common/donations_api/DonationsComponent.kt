package com.listentoprabhupada.common.donations_api

import com.listentoprabhupada.common.data.Donation
import com.arkivanov.decompose.value.Value

interface DonationsComponent {
    val flow: Value<DonationsState>

    fun purchase(donation: Donation) {}
}
