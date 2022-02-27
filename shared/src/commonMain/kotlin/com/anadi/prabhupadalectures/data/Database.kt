package com.anadi.prabhupadalectures.data

import com.anadi.prabhupadalectures.data.lectures.Lecture

interface Database {
    fun clearDatabase()

    fun saveDownload(lecture: Lecture, fileUrl: String? = null)
    fun isDownloaded(id: Long): Boolean
    fun getAllDownloads(): List<Download>
    fun removeDownload(id: Long)
    fun removeAllDownloads()

    fun setFavorite(id: Long)
    fun isFavorite(id: Long): Boolean
    fun getAllFavorites(): List<Long>
    fun removeFavorite(id: Long)
    fun removeAllFavorites()

    fun savePosition(id: Long, pos: Long)
    fun getSavedPosition(id: Long): Long
    fun removeSavedPosition(id: Long)
    fun removeAllSavedPosition()
}