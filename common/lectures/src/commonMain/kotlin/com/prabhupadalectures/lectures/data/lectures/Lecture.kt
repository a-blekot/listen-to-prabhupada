package com.prabhupadalectures.lectures.data.lectures

import com.prabhupadalectures.common.database.LectureEntity
import com.prabhupadalectures.lectures.fileSystem
import com.prabhupadalectures.lectures.utils.DOWNLOADS_DIR
import com.prabhupadalectures.lectures.utils.FILE_EXTENSION
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okio.Path.Companion.DIRECTORY_SEPARATOR
import okio.Path.Companion.toPath

const val LECTURE_KEY_ID = "LECTURE_KEY_ID"
const val LECTURE_KEY_TITLE = "LECTURE_KEY_TITLE"
const val LECTURE_KEY_DESCRIPTION = "LECTURE_KEY_DESCRIPTION"
const val LECTURE_KEY_DATE = "LECTURE_KEY_DATE"
const val LECTURE_KEY_PLACE = "LECTURE_KEY_PLACE"
const val LECTURE_KEY_DURATION = "LECTURE_KEY_DURATION"
const val LECTURE_KEY_FILE_URL = "LECTURE_KEY_FILE_URL"
const val LECTURE_KEY_REMOTE_URL = "LECTURE_KEY_REMOTE_URL"
const val LECTURE_KEY_IS_FAVORITE = "LECTURE_KEY_IS_FAVORITE"
const val LECTURE_KEY_DOWNLOAD_PROGRESS = "LECTURE_KEY_DOWNLOAD_PROGRESS"

data class Lecture(
    val id: Long = 0L,
    val title: String = "",
    val description: String? = null,
    val date: String = "",
    val place: String = "",
    val durationMillis: Long = 0L,
    val fileUrl: String? = null,
    val remoteUrl: String = "",
    val isFavorite: Boolean = false,
    val isCompleted: Boolean = false,
    val downloadProgress: Int? = null
) {
    constructor(lecture: LectureFullModel) : this(
        id = lecture.id,
        title = lecture.title,
        description = lecture.description,
        date = lecture.date,
        place = lecture.place,
        durationMillis = lecture.fileInfo.durationMillis,
        remoteUrl = lecture.fileInfo.mediaStreamUrl
    )

    constructor(lectureEntity: LectureEntity) : this(
        id = lectureEntity.id,
        title = lectureEntity.title,
        description = lectureEntity.description,
        date = lectureEntity.date,
        place = lectureEntity.place,
        durationMillis = lectureEntity.durationMillis,
        fileUrl = lectureEntity.fileUrl,
        remoteUrl = lectureEntity.remoteUrl,
        isFavorite = lectureEntity.isFavorite,
        isCompleted = lectureEntity.isCompleted,
        downloadProgress = lectureEntity.downloadProgress?.toInt()
    )

    val subTitle
        get() = "$date, $place"

    val displayedDescription
        get() = description ?: subTitle
}

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
    map { Lecture(it) }
