package com.prabhupadalectures.common.lectures_impl

import com.prabhupadalectures.common.utils.Lecture
import com.prabhupadalectures.common.lectures_impl.utils.ShareAction
import io.ktor.utils.io.*
import okio.FileSystem

expect class Platform() {
    val platform: String
}

expect val Lecture.fileSystem: FileSystem

expect fun Lecture.writeChannel(): ByteWriteChannel

expect val ShareAction.deepLink: String