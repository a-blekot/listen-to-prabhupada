package com.prabhupadalectures.common.lectures_impl.utils

const val DEEP_LINK_SCHEME = "https"
const val HOST_LECTURE = "lecture"

data class ShareAction(
    val lectureId: Long,
    val queryParams: String?,
    val timeMs: Long?
)