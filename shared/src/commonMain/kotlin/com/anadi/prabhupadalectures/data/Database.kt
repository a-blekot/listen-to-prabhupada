package com.anadi.prabhupadalectures.data

import com.anadi.prabhupadalectures.data.lectures.Lecture

class Database(databaseDriverFactory: DatabaseDriverFactory) {
    private val database = AppDatabase(databaseDriverFactory.createDriver())
    private val dbQuery = database.appDatabaseQueries

    fun clearDatabase() {
        dbQuery.transaction {
            dbQuery.removeAllDownload()
            dbQuery.removeAllFavorite()
            dbQuery.removeAllSavedPosition()
        }
    }

    fun saveDownload(lecture: Lecture, fileUrl: String? = null) =
        dbQuery.insertDownload(
            id = lecture.id,
            title = lecture.title,
            description = lecture.description,
            date = lecture.date,
            place = lecture.place,
            durationMillis = lecture.fileInfo.durationMillis,
            fileUrl = fileUrl
        )

    fun isDownloaded(id: Long) =
        dbQuery.selectDownload(id = id).executeAsList().isNotEmpty()

    fun getAllDownloads() =
        dbQuery.selectAllDownload().executeAsList()

    fun removeDownload(id: Long) =
        dbQuery.removeDownload(id = id)

    fun removeAllDownloads() =
        dbQuery.removeAllDownload()

    fun setFavorite(id: Long) =
        dbQuery.insertFavorite(id = id)

    fun isFavorite(id: Long) =
        dbQuery.selectFavorite(id = id).executeAsList().isNotEmpty()

    fun getAllFavorites() =
        dbQuery.selectAllFavorite().executeAsList()

    fun removeFavorite(id: Long) =
        dbQuery.removeFavorite(id = id)

    fun removeAllFavorites() =
        dbQuery.removeAllFavorite()

    fun savePosition(id: Long, pos: Long) =
        dbQuery.insertSavedPosition(id = id, pos = pos)

    fun getSavedPosition(id: Long) =
        dbQuery.selectSavedPosition(id = id).executeAsOneOrNull()

    fun removeSavedPosition(id: Long) =
        dbQuery.removeSavedPosition(id = id)

    fun removeAllSavedPosition() =
        dbQuery.removeAllSavedPosition()
}