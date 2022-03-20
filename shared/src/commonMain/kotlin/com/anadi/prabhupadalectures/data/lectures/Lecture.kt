package com.anadi.prabhupadalectures.data.lectures

import com.anadi.prabhupadalectures.data.CachedLecture
import com.anadi.prabhupadalectures.network.api.FULL_PROGRESS
import com.anadi.prabhupadalectures.utils.DOWNLOADS_DIR
import com.anadi.prabhupadalectures.utils.FILE_EXTENSION
import com.anadi.prabhupadalectures.utils.toBoolean
import java.io.File

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

    constructor(cachedLecture: CachedLecture) : this(
        id = cachedLecture.id,
        title = cachedLecture.title,
        description = cachedLecture.description,
        date = cachedLecture.date,
        place = cachedLecture.place,
        durationMillis = cachedLecture.durationMillis,
        fileUrl = cachedLecture.fileUrl,
        remoteUrl = cachedLecture.remoteUrl,
        isFavorite = cachedLecture.isFavorite.toBoolean(),
        isCompleted = cachedLecture.isCompleted.toBoolean(),
        downloadProgress = cachedLecture.downloadProgress?.toInt()
    )

    val subTitle
        get() = "$date, $place"

    val displayedDescription
        get() = description ?: "no description"

    val isDownloaded
        get() = fileUrl != null && downloadProgress == FULL_PROGRESS
}

val Lecture.file: File
    get() = File(DOWNLOADS_DIR, fileName)

private val Lecture.fileName
    get() = "$title.$FILE_EXTENSION"
