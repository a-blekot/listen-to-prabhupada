package com.listentoprabhupada.common.settings_impl.store

import com.arkivanov.mvikotlin.core.store.Store
import com.listentoprabhupada.common.settings_api.SettingsState

internal interface SettingsStore : Store<SettingsIntent, SettingsState, SettingsLabel>
