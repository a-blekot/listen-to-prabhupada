package com.listentoprabhupada.common.results_impl

import com.listentoprabhupada.common.data.Lecture
import com.listentoprabhupada.common.results_impl.data.filePath
import io.ktor.util.cio.writeChannel
import io.ktor.utils.io.*
import okio.FileSystem
import java.io.File

actual fun Lecture.writeChannel(): ByteWriteChannel =
    File(filePath.toString()).writeChannel()

actual val Lecture.fileSystem: FileSystem
    get() = FileSystem.SYSTEM






