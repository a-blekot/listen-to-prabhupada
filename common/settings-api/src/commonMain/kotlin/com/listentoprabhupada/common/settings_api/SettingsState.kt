package com.listentoprabhupada.common.settings_api

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.listentoprabhupada.common.data.VersionInfo

@Parcelize
data class SettingsState(
    val locale: String,
    val versionInfo: VersionInfo
): Parcelable
