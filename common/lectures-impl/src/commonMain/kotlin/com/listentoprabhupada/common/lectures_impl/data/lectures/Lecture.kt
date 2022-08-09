package com.listentoprabhupada.common.lectures_impl.data.lectures

import com.listentoprabhupada.common.data.Lecture
import com.listentoprabhupada.common.lectures_impl.fileSystem
import com.listentoprabhupada.common.lectures_impl.utils.DOWNLOADS_DIR
import com.listentoprabhupada.common.lectures_impl.utils.FILE_EXTENSION
import okio.Path.Companion.DIRECTORY_SEPARATOR
import okio.Path.Companion.toPath

fun Lecture.exists() =
    fileSystem.exists(filePath)

val Lecture.filePath
    get() = "$DOWNLOADS_DIR$DIRECTORY_SEPARATOR$fileName".toPath()

val Lecture.fileName
    get() = "$title.$FILE_EXTENSION"
