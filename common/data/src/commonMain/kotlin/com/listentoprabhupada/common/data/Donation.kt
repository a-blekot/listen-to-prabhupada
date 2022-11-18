package com.listentoprabhupada.common.data

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize

@Parcelize
data class Donation(
    val donationLevel: DonationLevel,
    val formattedPrice: String,
    val priceAmountMicros: Long,
    val title: String = "",
): Parcelable
