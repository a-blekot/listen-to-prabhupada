package com.prabhupadalectures.common.database

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList

private const val FULL_PROGRESS = 100L
const val FIRST_PAGE = 1

class DatabaseImpl(databaseDriverFactory: DatabaseDriverFactory) : Database {
    private val database = AppDatabase(databaseDriverFactory.createDriver())
    private val dbQuery = database.appDatabaseQueries

    override fun clearDatabase() {
        dbQuery.transaction {
            dbQuery.deleteAllLectures()
            dbQuery.deleteAllSavedPositions()
            dbQuery.deleteAllExpandedFilters()
        }
    }

    override fun insertLecture(lecture: LectureEntity) =
        lecture.run {
            dbQuery.insertLecture(
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
                downloadProgress = downloadProgress,
            )
        }

    override fun selectLecture(id: Long) =
        dbQuery.selectLecture(id = id)
            .executeAsList()
            .getOrNull(0)

    override fun selectAllDownloads() =
        dbQuery.selectAllDownloads()
            .executeAsList()

    override fun selectAllFavorites() =
        dbQuery.selectAllFavorites()
            .executeAsList()

    override fun observeAllDownloads() =
        dbQuery.selectAllDownloads()
            .asFlow()
            .mapToList()

    override fun observeAllFavorites() =
        dbQuery.selectAllFavorites()
            .asFlow()
            .mapToList()

    override fun observeCompleted() =
        dbQuery.selectAllCompleted()
            .asFlow()
            .mapToList()

    override fun deleteLecture(id: Long) =
        dbQuery.deleteLecture(id = id)

    override fun deleteFromDownloadsOnly(lecture: LectureEntity) =
        if (selectLecture(lecture.id)?.isFavorite == true) {
            insertLecture(
                lecture.copy(fileUrl = null, downloadProgress = null)
            )
        } else {
            deleteLecture(lecture.id)
        }

    override fun deleteFromFavoritesOnly(lecture: LectureEntity) =
        selectLecture(lecture.id)?.run {
            if (isDownloaded) {
                insertLecture(lecture.copy(isFavorite = false))
            } else {
                deleteLecture(lecture.id)
            }
        } ?: Unit

    override fun deleteAllLectures() =
        dbQuery.deleteAllLectures()

    override fun insertPage(id: Long, page: Int) =
        dbQuery.insertPage(id = id, page = page.toLong())

    override fun selectPage(id: Long) =
        dbQuery.selectPage(id = id)
            .executeAsOneOrNull()
            ?.page?.toInt() ?: FIRST_PAGE

    override fun deletePage(id: Long) =
        dbQuery.deletePage(id = id)

    override fun deleteAllPages() =
        dbQuery.deleteAllPages()

    override fun insertSavedPosition(id: Long, pos: Long) =
        dbQuery.insertSavedPosition(id = id, pos = pos)

    override fun selectSavedPosition(id: Long) =
        dbQuery.selectSavedPosition(id = id)
            .executeAsOneOrNull()
            ?.pos ?: 0L

    override fun deleteSavedPosition(id: Long) =
        dbQuery.deleteSavedPosition(id = id)

    override fun deleteAllSavedPositions() =
        dbQuery.deleteAllSavedPositions()

    override fun selectExpandedFilter(filterName: String) =
        dbQuery.selectExpandedFilter(id = filterName.toLong())
            .executeAsOneOrNull()
            ?.expanded ?: false

    override fun insertExpandedFilter(filterName: String, isExpanded: Boolean) =
        dbQuery.insertExpandedFilter(id = filterName.toLong(), isExpanded)

    override fun deleteAllExpandedFilters() =
        dbQuery.deleteAllExpandedFilters()
}

fun String.toLong() = hashCode().toLong()

val LectureEntity.isDownloaded
    get() = fileUrl != null && downloadProgress == FULL_PROGRESS