package com.listentoprabhupada.common.lectures_impl

import com.listentoprabhupada.common.data.Lecture
import io.ktor.utils.io.*
import okio.FileSystem
import platform.UIKit.UIDevice

actual fun Lecture.writeChannel(): ByteWriteChannel {
    TODO("Not yet implemented")
}

actual val Lecture.fileSystem: FileSystem
    get() = FileSystem.SYSTEM
