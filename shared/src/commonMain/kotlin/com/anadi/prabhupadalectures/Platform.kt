package com.anadi.prabhupadalectures

import com.anadi.prabhupadalectures.data.lectures.Lecture
import io.ktor.utils.io.*

expect class Platform() {
    val platform: String
}

expect fun Lecture.writeChannel(): ByteWriteChannel