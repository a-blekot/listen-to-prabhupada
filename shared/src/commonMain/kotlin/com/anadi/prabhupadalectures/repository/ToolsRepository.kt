package com.anadi.prabhupadalectures.repository

import com.anadi.prabhupadalectures.data.Database
import com.anadi.prabhupadalectures.data.lectures.Lecture
import java.io.Serializable

interface ToolsRepository {
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

    override fun setFavorite(lecture: Lecture, isFavorite: Boolean) =
        db.insertCachedLecture(lecture.copy(isFavorite = isFavorite))
    
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