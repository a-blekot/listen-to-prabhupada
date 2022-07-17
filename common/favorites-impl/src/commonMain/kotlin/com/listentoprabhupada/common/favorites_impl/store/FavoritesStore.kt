package com.listentoprabhupada.common.favorites_impl.store

import com.arkivanov.mvikotlin.core.store.Store
import com.listentoprabhupada.common.favorites_api.FavoritesState

internal interface FavoritesStore : Store<FavoritesIntent, FavoritesState, FavoritesLabel>
