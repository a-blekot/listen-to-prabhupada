package com.prabhupadalectures.common.lectures_impl.data.lectures

import com.prabhupadalectures.common.network_api.Routes
import io.github.aakira.napier.Napier

private const val NO_DURATION = -1L

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

    private var _durationMillis: Long = NO_DURATION
    val durationMillis: Long
        get() {
            if (_durationMillis != NO_DURATION) {
                return _durationMillis
            }

            _durationMillis = duration.parseDuration()
            return _durationMillis
        }
}

// 00:03:09.072000
fun String.parseDuration() =
    try {
        val arr = split(":")
        val hours = arr.getOrNull(0)?.toLong() ?: 0L
        val minutes = arr.getOrNull(1)?.toLong() ?: 0L
        val seconds = arr.getOrNull(2)?.toFloat() ?: 0F

        hours * 3_600_000 + minutes * 60_000 + (seconds * 1000).toLong()
    } catch (e: NumberFormatException) {
        Napier.e("failed to compute durationMillis", e)
        0L
    }
