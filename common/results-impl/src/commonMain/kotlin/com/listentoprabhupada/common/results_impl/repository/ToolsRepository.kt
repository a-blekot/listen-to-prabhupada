package com.listentoprabhupada.common.results_impl.repository

import com.listentoprabhupada.common.database.Database
import com.listentoprabhupada.common.settings.getNextNotificationId

import com.listentoprabhupada.common.data.Lecture
import com.listentoprabhupada.common.utils.dbEntity
import com.listentoprabhupada.common.utils.mapped
import kotlinx.coroutines.flow.Flow

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

    override fun observeFavorites() =
        db.observeAllFavorites().mapped()

    override fun observeDownloads() =
        db.observeAllDownloads().mapped()

    override fun setFavorite(lecture: Lecture, isFavorite: Boolean) =
        db.insertLecture(lecture.copy(isFavorite = isFavorite).dbEntity())

    override fun setCompleted(id: Long) {
        db.insertCompleted(id, isCompleted = true)
        db.insertSavedPosition(id, 0)
    }

    override fun savePosition(id: Long, timeMs: Long) =
        db.insertSavedPosition(id, timeMs)

    override fun getPosition(id: Long) =
        db.selectSavedPosition(id)

    override fun isExpanded(filterName: String): Boolean =
        db.selectExpandedFilter(filterName)

    override fun saveExpanded(filterName: String, isExpanded: Boolean) =
        db.insertExpandedFilter(filterName, isExpanded)

    override fun getNextNotificationId() =
        com.listentoprabhupada.common.settings.settings.getNextNotificationId()
}