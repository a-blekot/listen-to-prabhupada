package com.listentoprabhupada.common.results_impl

import com.listentoprabhupada.common.data.Lecture
import io.ktor.utils.io.*
import okio.FileSystem

expect val Lecture.fileSystem: FileSystem

expect fun Lecture.writeChannel(): ByteWriteChannel
