package com.prabhupadalectures.common.lectures_impl.data.lectures

import com.prabhupadalectures.common.utils.Lecture
import com.prabhupadalectures.common.lectures_impl.fileSystem
import com.prabhupadalectures.common.lectures_impl.utils.DOWNLOADS_DIR
import com.prabhupadalectures.common.lectures_impl.utils.FILE_EXTENSION
import okio.Path.Companion.DIRECTORY_SEPARATOR
import okio.Path.Companion.toPath

fun Lecture.exists() =
    fileSystem.exists(filePath)

val Lecture.filePath
    get() = "$DOWNLOADS_DIR$DIRECTORY_SEPARATOR$fileName".toPath()

val Lecture.fileName
    get() = "$title.$FILE_EXTENSION"
