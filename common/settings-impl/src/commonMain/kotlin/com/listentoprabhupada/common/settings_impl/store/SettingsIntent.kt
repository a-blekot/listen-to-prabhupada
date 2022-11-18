package com.listentoprabhupada.common.settings_impl.store

sealed interface SettingsIntent {
    data class Locale(val value: String) : SettingsIntent
}
