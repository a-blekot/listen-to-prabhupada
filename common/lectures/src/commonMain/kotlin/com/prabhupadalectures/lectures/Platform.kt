package com.prabhupadalectures.lectures

import com.prabhupadalectures.lectures.data.lectures.Lecture
import com.prabhupadalectures.lectures.utils.ShareAction
import io.ktor.utils.io.*
import okio.FileSystem

expect class Platform() {
    val platform: String
}

expect val Lecture.fileSystem: FileSystem

expect fun Lecture.writeChannel(): ByteWriteChannel

expect val ShareAction.deepLink: String