package com.listentoprabhupada.common.database

import kotlinx.coroutines.flow.Flow

interface Database {
    fun clearDatabase()

    fun insertLecture(lecture: LectureEntity)
    fun selectLecture(id: Long): LectureEntity?
    fun selectAllDownloads(): List<LectureEntity>
    fun selectAllFavorites(): List<LectureEntity>
    fun observeAllDownloads(): Flow<List<LectureEntity>>
    fun observeAllFavorites(): Flow<List<LectureEntity>>
    fun observeCompleted(): Flow<List<Boolean>>
    fun deleteLecture(id: Long)
    fun deleteFromDownloadsOnly(lecture: LectureEntity)
    fun deleteFromFavoritesOnly(lecture: LectureEntity)
    fun deleteAllLectures()

    fun insertPage(id: Long, page: Int)
    fun selectPage(id: Long): Int
    fun deletePage(id: Long)
    fun deleteAllPages()

    fun insertSavedPosition(id: Long, pos: Long)
    fun selectSavedPosition(id: Long): Long
    fun deleteSavedPosition(id: Long)
    fun deleteAllSavedPositions()

    fun insertCompleted(id: Long, isCompleted: Boolean)
    fun selectCompleted(id: Long): Boolean?
    fun deleteCompleted(id: Long)
    fun deleteAllCompleted()

    fun insertExpandedFilter(filterName: String, isExpanded: Boolean)
    fun selectExpandedFilter(filterName: String): Boolean
    fun deleteAllExpandedFilters()
}