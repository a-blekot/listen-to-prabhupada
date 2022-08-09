package com.listentoprabhupada.common.utils

import com.listentoprabhupada.common.data.Lecture
import com.listentoprabhupada.common.database.LectureEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

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