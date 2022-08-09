package com.listentoprabhupada.common.utils

actual val ShareAction.deepLink: String
    get() {
        var result = "$DEEP_LINK_SCHEME://$HOST_LECTURE/$lectureId"

        if (queryParams != null) {
            result += "?$queryParams"
        }

        if (timeMs != null) {
            result += "#$timeMs"
        }

        return result
    }