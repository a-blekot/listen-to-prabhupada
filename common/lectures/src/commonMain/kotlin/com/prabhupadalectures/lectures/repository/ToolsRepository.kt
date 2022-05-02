package com.prabhupadalectures.lectures.repository

import com.prabhupadalectures.common.database.Database
import com.prabhupadalectures.lectures.data.lectures.Lecture
import com.prabhupadalectures.lectures.data.lectures.dbEntity
import com.prabhupadalectures.lectures.data.lectures.mapped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface ToolsRepository {
    fun setCompleted(id: Long)
    fun observeFavorites(): Flow<List<Lecture>>
    fun observeDownloads(): Flow<List<Lecture>>
    fun setFavorite(lecture: Lecture, isFavorite: Boolean)
    fun savePosition(id: Long, timeMs: Long)
    fun getPosition(id: Long): Long
    fun isExpanded(filterName: String): Boolean
    fun saveExpanded(filterName: String, isExpanded: Boolean)
    fun getNextNotificationId(): Int
}

class ToolsRepositoryImpl(
    private val db: Database
) : ToolsRepository {

    override fun setCompleted(id: Long) =
        db.selectLecture(id)?.let {
            db.insertLecture(it.copy(isCompleted = true))
        } ?: Unit

    override fun observeFavorites() =
        db.observeAllFavorites().mapped()

    override fun observeDownloads() =
        db.observeAllDownloads().mapped()

    override fun setFavorite(lecture: Lecture, isFavorite: Boolean) =
        db.insertLecture(lecture.copy(isFavorite = isFavorite).dbEntity())
    
    override fun savePosition(id: Long, timeMs: Long) =
        db.insertSavedPosition(id, timeMs)

    override fun getPosition(id: Long) =
        db.selectSavedPosition(id)

    override fun isExpanded(filterName: String): Boolean =
        db.selectExpandedFilter(filterName)

    override fun saveExpanded(filterName: String, isExpanded: Boolean) =
        db.insertExpandedFilter(filterName, isExpanded)

    override fun getNextNotificationId() =
        settings.getNextNotificationId()
}