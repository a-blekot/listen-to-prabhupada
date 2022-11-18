package com.listentoprabhupada.common.settings_impl

import com.listentoprabhupada.common.data.VersionInfo
import com.listentoprabhupada.common.utils.analytics.Analytics
import com.listentoprabhupada.common.utils.dispatchers.DispatcherProvider

data class SettingsDeps(
    val analytics: Analytics,
    val dispatchers: DispatcherProvider,
    val versionInfo: VersionInfo,
)
