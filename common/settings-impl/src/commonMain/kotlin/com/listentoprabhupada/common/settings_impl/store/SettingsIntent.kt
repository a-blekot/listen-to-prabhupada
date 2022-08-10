package com.listentoprabhupada.common.settings_impl.store

sealed interface SettingsIntent {
    object Next : SettingsIntent
    object Prev : SettingsIntent
}
