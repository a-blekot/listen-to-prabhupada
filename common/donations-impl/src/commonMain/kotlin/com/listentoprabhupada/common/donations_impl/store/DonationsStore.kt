package com.listentoprabhupada.common.donations_impl.store

import com.arkivanov.mvikotlin.core.store.Store
import com.listentoprabhupada.common.donations_api.DonationsState

internal interface DonationsStore : Store<DonationsIntent, DonationsState, DonationsLabel>
