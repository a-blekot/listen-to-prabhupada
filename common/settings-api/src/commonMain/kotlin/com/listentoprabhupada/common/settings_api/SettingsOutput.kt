package com.listentoprabhupada.common.settings_api

sealed interface SettingsOutput {
    object Email: SettingsOutput
    object ShareApp: SettingsOutput
    object RateUs: SettingsOutput
    object Donations: SettingsOutput
    object Back: SettingsOutput
}