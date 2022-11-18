package com.listentoprabhupada.common.utils.resources

import android.content.Context
import com.listentoprabhupada.common.data.DonationLevel

class AndroidStringResourceHandler(private val context: Context): StringResourceHandler {
    override fun resolveDonationTitle(level: DonationLevel) =
        donationsMap[level]?.apply {

        } ?: throw IllegalArgumentException("DonationLevel $level does not exist!")
}