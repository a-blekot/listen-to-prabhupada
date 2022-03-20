package com.anadi.prabhupadalectures

import com.anadi.prabhupadalectures.data.lectures.Lecture
import com.anadi.prabhupadalectures.data.lectures.file
import io.ktor.util.cio.*
import io.ktor.utils.io.*
import java.io.File

actual class Platform actual constructor() {
    actual val platform: String = "Android ${android.os.Build.VERSION.SDK_INT}"
}

actual fun Lecture.writeChannel(): ByteWriteChannel =
    file.writeChannel()