package com.listentoprabhupada.common.settings_api

import com.arkivanov.decompose.value.Value

interface SettingsComponent {

    val flow: Value<SettingsState>

    fun setLocale(value: String) {}
    fun onShowTutorial() {}
    fun onTutorialCompleted() {}
    fun sendEmail() {}
    fun shareApp() {}
    fun rateUs() {}
    fun donations() {}
    fun back() {}
}
