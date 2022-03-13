package com.anadi.prabhupadalectures.data

import com.anadi.prabhupadalectures.data.lectures.Lecture
import com.anadi.prabhupadalectures.utils.toBoolean
import com.anadi.prabhupadalectures.utils.toLong

class DatabaseImpl(databaseDriverFactory: DatabaseDriverFactory) : Database {
    private val database = AppDatabase(databaseDriverFactory.createDriver())
    private val dbQuery = database.appDatabaseQueries

    override fun clearDatabase() {
        dbQuery.transaction {
            dbQuery.removeAllDownload()
            dbQuery.removeAllFavorite()
            dbQuery.removeAllSavedPosition()
        }
    }

    override fun saveDownload(lecture: Lecture, fileUrl: String?) =
        dbQuery.insertDownload(
            id = lecture.id,
            title = lecture.title,
            description = lecture.description,
            date = lecture.date,
            place = lecture.place,
            durationMillis = lecture.fileInfo.durationMillis,
            fileUrl = fileUrl
        )

    override fun isDownloaded(id: Long) =
        dbQuery.selectDownload(id = id).executeAsList().isNotEmpty()

    override fun getAllDownloads() =
        dbQuery.selectAllDownload().executeAsList()

    override fun removeDownload(id: Long) =
        dbQuery.removeDownload(id = id)

    override fun removeAllDownloads() =
        dbQuery.removeAllDownload()

    override fun setFavorite(id: Long) =
        dbQuery.insertFavorite(id = id)

    override fun isFavorite(id: Long) =
        dbQuery.selectFavorite(id = id).executeAsList().isNotEmpty()

    override fun getAllFavorites() =
        dbQuery.selectAllFavorite().executeAsList()

    override fun removeFavorite(id: Long) =
        dbQuery.removeFavorite(id = id)

    override fun removeAllFavorites() =
        dbQuery.removeAllFavorite()

    override fun savePosition(id: Long, pos: Long) =
        dbQuery.insertSavedPosition(id = id, pos = pos)

    override fun getSavedPosition(id: Long) =
        dbQuery.selectSavedPosition(id = id).executeAsOneOrNull()?.pos ?: 0L

    override fun removeSavedPosition(id: Long) =
        dbQuery.removeSavedPosition(id = id)

    override fun removeAllSavedPosition() =
        dbQuery.removeAllSavedPosition()

    override fun isExpanded(filterName: String) =
        dbQuery.selectExpandedFilter(id = filterName.toLong()).executeAsOneOrNull()?.expanded?.toBoolean() ?: true

    override fun saveExpanded(filterName: String, isExpanded: Boolean) =
        dbQuery.insertExpandedFilter(id = filterName.toLong(), isExpanded.toLong())
}
