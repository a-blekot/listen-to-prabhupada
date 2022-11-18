package com.listentoprabhupada.common.results_impl

import com.listentoprabhupada.common.data.Lecture
import com.listentoprabhupada.common.results_impl.data.filePath
import io.ktor.utils.io.*
import okio.FileSystem

actual fun Lecture.writeChannel() =
    byteWriteChannel(filePath.toString())

actual val Lecture.fileSystem: FileSystem
    get() = PosixFileSystem()
