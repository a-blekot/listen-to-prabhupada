package com.listentoprabhupada.common.donations_api

sealed interface DonationsOutput {
    object SuccessPurchase : DonationsOutput
}
