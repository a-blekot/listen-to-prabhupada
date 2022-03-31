package com.anadi.prabhupadalectures

import com.anadi.prabhupadalectures.data.lectures.Lecture
import io.ktor.utils.io.*
import okio.FileSystem

expect class Platform() {
    val platform: String
}

expect val Lecture.fileSystem: FileSystem

expect fun Lecture.writeChannel(): ByteWriteChannel