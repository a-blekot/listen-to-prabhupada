package com.listentoprabhupada.common.utils.resources

import com.listentoprabhupada.common.data.DonationLevel

interface StringResourceHandler {
    @Throws(IllegalArgumentException::class)
    fun resolveDonationTitle(level: DonationLevel): String
}