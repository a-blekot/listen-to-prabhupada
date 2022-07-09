package com.prabhupadalectures.common.downloads_impl.store

import com.arkivanov.mvikotlin.core.store.Store
import com.prabhupadalectures.common.downloads_api.DownloadsState

internal interface DownloadsStore : Store<DownloadsIntent, DownloadsState, DownloadsLabel>
