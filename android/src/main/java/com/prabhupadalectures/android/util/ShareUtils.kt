package com.prabhupadalectures.android.util

import android.net.Uri
import com.prabhupadalectures.lectures.utils.DEEP_LINK_SCHEME
import com.prabhupadalectures.lectures.utils.HOST_LECTURE
import com.prabhupadalectures.lectures.utils.ShareAction

//  listenprabhupada://lecture/{lectureId}?page=2&category=1#1233412
//  uri.lastPathSegment {lectureId}
//  uri.encodedQuery - page=2&category=1 - QueryParams (selected filters)
//  uri.fragment - 1233412L - playback in millis

fun parseShareAction(uri: Uri?): ShareAction? =
    uri?.run {
        if (scheme != DEEP_LINK_SCHEME) return null
        if (host != HOST_LECTURE) return null

        val lectureId = lastPathSegment?.toLongOrNull() ?: return null
        val queryParams = encodedQuery
        val timeMs = uri.fragment?.toLongOrNull()

        ShareAction(lectureId, queryParams, timeMs)
    }
