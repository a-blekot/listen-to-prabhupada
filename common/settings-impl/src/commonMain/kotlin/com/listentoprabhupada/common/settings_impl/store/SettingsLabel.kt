package com.listentoprabhupada.common.settings_impl.store

sealed interface SettingsLabel {
    data class Message(val text: String) : SettingsLabel
}
