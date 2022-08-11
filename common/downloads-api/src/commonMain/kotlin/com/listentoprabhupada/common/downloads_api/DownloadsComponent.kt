package com.listentoprabhupada.common.downloads_api

import com.arkivanov.decompose.value.Value
import com.listentoprabhupada.common.data.LectureComponent

interface DownloadsComponent: LectureComponent {
    val flow: Value<DownloadsState>
}
