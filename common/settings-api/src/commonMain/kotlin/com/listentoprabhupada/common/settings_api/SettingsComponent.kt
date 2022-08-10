package com.listentoprabhupada.common.settings_api

import com.arkivanov.decompose.value.Value

interface SettingsComponent {

    val flow: Value<SettingsState>

    fun onNext() {}
    fun onPrev() {}
}
