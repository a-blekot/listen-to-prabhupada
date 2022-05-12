package com.prabhupadalectures.common.lectures_impl.data.lectures

import com.prabhupadalectures.common.database.LectureEntity
import com.prabhupadalectures.common.lectures_api.Lecture
import com.prabhupadalectures.common.lectures_impl.fileSystem
import com.prabhupadalectures.common.lectures_impl.utils.DOWNLOADS_DIR
import com.prabhupadalectures.common.lectures_impl.utils.FILE_EXTENSION
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okio.Path.Companion.DIRECTORY_SEPARATOR
import okio.Path.Companion.toPath

fun Lecture.exists() =
    fileSystem.exists(filePath)

val Lecture.filePath
    get() = "$DOWNLOADS_DIR$DIRECTORY_SEPARATOR$fileName".toPath()

val Lecture.fileName
    get() = "$title.$FILE_EXTENSION"

fun Lecture.dbEntity(): LectureEntity =
    LectureEntity(
        id = id,
        title = title,
        description = description,
        date = date,
        place = place,
        durationMillis = durationMillis,
        fileUrl = fileUrl,
        remoteUrl = remoteUrl,
        isFavorite = isFavorite,
        isCompleted = isCompleted,
        downloadProgress = downloadProgress?.toLong()
    )

fun Flow<List<LectureEntity>>.mapped() =
    map { it.mapped() }

fun List<LectureEntity>.mapped() =
    map {
        Lecture(
            id = it.id,
            title = it.title,
            description = it.description,
            date = it.date,
            place = it.place,
            durationMillis = it.durationMillis,
            fileUrl = it.fileUrl,
            remoteUrl = it.remoteUrl,
            isFavorite = it.isFavorite,
            isCompleted = it.isCompleted,
            downloadProgress = it.downloadProgress?.toInt()
        )
    }
