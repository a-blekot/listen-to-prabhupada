package com.prabhupadalectures.common.favorites_impl.store

import com.arkivanov.mvikotlin.core.store.Store
import com.prabhupadalectures.common.favorites_api.FavoritesState

internal interface FavoritesStore : Store<FavoritesIntent, FavoritesState, FavoritesLabel>
