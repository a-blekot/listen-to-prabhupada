package com.prabhupadalectures.lectures

import com.prabhupadalectures.lectures.data.lectures.Lecture
import com.prabhupadalectures.lectures.data.lectures.filePath
import com.prabhupadalectures.lectures.utils.DEEP_LINK_SCHEME
import com.prabhupadalectures.lectures.utils.HOST_LECTURE
import com.prabhupadalectures.lectures.utils.ShareAction
import io.ktor.util.cio.*
import io.ktor.utils.io.*
import okio.FileSystem
import java.io.File

actual class Platform actual constructor() {
    actual val platform: String = "Android ${android.os.Build.VERSION.SDK_INT}"
}

actual fun Lecture.writeChannel(): ByteWriteChannel =
    File(filePath.toString()).writeChannel()

actual val Lecture.fileSystem: FileSystem
    get() = FileSystem.SYSTEM

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





