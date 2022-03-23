package com.anadi.prabhupadalectures.data

import com.anadi.prabhupadalectures.data.lectures.Lecture
import com.anadi.prabhupadalectures.data.lectures.LectureFullModel
import com.anadi.prabhupadalectures.network.api.FULL_PROGRESS
import com.anadi.prabhupadalectures.repository.FIRST_PAGE
import com.anadi.prabhupadalectures.utils.toBoolean
import com.anadi.prabhupadalectures.utils.toLong
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DatabaseImpl(databaseDriverFactory: DatabaseDriverFactory) : Database {
    private val database = AppDatabase(databaseDriverFactory.createDriver())
    private val dbQuery = database.appDatabaseQueries

    override fun clearDatabase() {
        dbQuery.transaction {
            dbQuery.deleteAllCachedLectures()
            dbQuery.deleteAllSavedPositions()
            dbQuery.deleteAllExpandedFilters()
        }
    }

    override fun insertCachedLecture(lecture: Lecture) =
        lecture.run {
            dbQuery.insertCachedLecture(
                id = id,
                title = title,
                description = description,
                date = date,
                place = place,
                durationMillis = durationMillis,
                fileUrl = fileUrl ?: "",
                remoteUrl = remoteUrl,
                isFavorite = isFavorite.toLong(),
                isCompleted = isCompleted.toLong(),
                downloadProgress = downloadProgress?.toLong(),
            )
        }

    override fun selectCachedLecture(id: Long) =
        dbQuery.selectCachedLecture(id = id)
            .executeAsList()
            .map { Lecture(it) }
            .getOrNull(0)

    override fun selectAllDownloads() =
        dbQuery.selectAllDownloads()
            .executeAsList()
            .map { Lecture(it) }

    override fun selectAllFavorites() =
        dbQuery.selectAllFavorites()
            .executeAsList()
            .map { Lecture(it) }

    override fun observeAllDownloads() =
        dbQuery.selectAllDownloads()
            .asFlow()
            .mapToList()
            .map { list -> list.map { Lecture(it) } }

    override fun observeAllFavorites() =
        dbQuery.selectAllFavorites()
            .asFlow()
            .mapToList()
            .map { list -> list.map { Lecture(it) } }

    override fun observeCompleted(): Flow<List<Lecture>> =
        dbQuery.selectAllCompleted()
            .asFlow()
            .mapToList()
            .map { list -> list.map { Lecture(it) } }

    override fun deleteCachedLecture(id: Long) =
        dbQuery.deleteCachedLecture(id = id)

    override fun deleteFromDownloadsOnly(lecture: Lecture) =
        if (selectCachedLecture(lecture.id)?.isFavorite == true) {
            insertCachedLecture(
                lecture.copy(fileUrl = null, downloadProgress = null)
            )
        } else {
            deleteCachedLecture(lecture.id)
        }

    override fun deleteFromFavoritesOnly(lecture: Lecture) =
        selectCachedLecture(lecture.id)?.run {
            if (isDownloaded) {
                insertCachedLecture(lecture.copy(isFavorite = false))
            } else {
                deleteCachedLecture(lecture.id)
            }
        } ?: Unit

    override fun deleteAllCachedLectures() =
        dbQuery.deleteAllCachedLectures()

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
            ?.expanded?.toBoolean() ?: true

    override fun insertExpandedFilter(filterName: String, isExpanded: Boolean) =
        dbQuery.insertExpandedFilter(id = filterName.toLong(), isExpanded.toLong())

    override fun deleteAllExpandedFilters() =
        dbQuery.deleteAllExpandedFilters()
}
