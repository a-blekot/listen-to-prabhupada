package com.listentoprabhupada.common.settings_api

sealed interface SettingsOutput {
    data class Message(val text: String) : SettingsOutput
}
