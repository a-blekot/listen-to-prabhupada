package com.prabhupadalectures.common.lectures_impl.utils

import kotlin.native.concurrent.SharedImmutable

@SharedImmutable
const val DEEP_LINK_SCHEME = "https"
@SharedImmutable
const val HOST_LECTURE = "lecture"

data class ShareAction(
    val lectureId: Long,
    val queryParams: String?,
    val timeMs: Long?
)