package com.listentoprabhupada.common.utils

const val DEEP_LINK_SCHEME = "https"
const val HOST_LECTURE = "lecture"

data class ShareAction(
    val lectureId: Long,
    val queryParams: String?,
    val timeMs: Long?
)