package com.listentoprabhupada.common.donations_impl.store

sealed interface DonationsLabel {
    object SuccessPurchase : DonationsLabel
}
