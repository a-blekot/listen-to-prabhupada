package com.listentoprabhupada.common.filters_impl.store

import com.arkivanov.mvikotlin.core.store.Store
import com.listentoprabhupada.common.filters_api.FiltersState

internal interface FiltersStore : Store<FiltersIntent, FiltersState, FiltersLabel>
