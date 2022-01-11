package com.anadi.prabhupadalectures.data.lectures

import com.anadi.prabhupadalectures.network.Routes

data class FileInfo(
    val fileType: FileType = FileType(),
    val mimeType: String = "",
    val audioQuality: Long = 0L,
    val videoQuality: Long? = null,

    val url: String = "",
    val md5: String = "",
    val size: Long = 0L,
    val duration: String = ""
) {
    val mediaStreamUrl
        get() = "${Routes.BASE_URL}$url"
}
