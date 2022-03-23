package com.anadi.prabhupadalectures.data

import com.anadi.prabhupadalectures.data.lectures.Lecture
import kotlinx.coroutines.flow.Flow

interface Database {
    fun clearDatabase()

    fun insertCachedLecture(lecture: Lecture)
    fun selectCachedLecture(id: Long): Lecture?
    fun selectAllDownloads(): List<Lecture>
    fun selectAllFavorites(): List<Lecture>
    fun observeAllDownloads(): Flow<List<Lecture>>
    fun observeAllFavorites(): Flow<List<Lecture>>
    fun observeCompleted(): Flow<List<Lecture>>
    fun deleteCachedLecture(id: Long)
    fun deleteFromDownloadsOnly(lecture: Lecture)
    fun deleteFromFavoritesOnly(lecture: Lecture)
    fun deleteAllCachedLectures()

    fun insertPage(id: Long, page: Int)
    fun selectPage(id: Long): Int
    fun deletePage(id: Long)
    fun deleteAllPages()

    fun insertSavedPosition(id: Long, pos: Long)
    fun selectSavedPosition(id: Long): Long
    fun deleteSavedPosition(id: Long)
    fun deleteAllSavedPositions()

    fun insertExpandedFilter(filterName: String, isExpanded: Boolean)
    fun selectExpandedFilter(filterName: String): Boolean
    fun deleteAllExpandedFilters()
}