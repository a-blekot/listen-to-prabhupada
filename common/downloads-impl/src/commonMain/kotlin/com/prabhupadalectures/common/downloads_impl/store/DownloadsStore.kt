package com.listentoprabhupada.common.downloads_impl.store

import com.arkivanov.mvikotlin.core.store.Store
import com.listentoprabhupada.common.downloads_api.DownloadsState

internal interface DownloadsStore : Store<DownloadsIntent, DownloadsState, DownloadsLabel>
