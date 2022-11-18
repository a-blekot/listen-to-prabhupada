package com.listentoprabhupada.common.data

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize

@Parcelize
data class VersionInfo(
    val versionCode: Int,
    val versionName: String
): Parcelable