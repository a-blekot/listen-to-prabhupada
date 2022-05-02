package com.prabhupadalectures.android.util

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import com.prabhupadalectures.android.BuildConfig
import com.prabhupadalectures.lectures.data.lectures.*
import java.io.File

private fun Context.isMyServiceRunning(serviceClass: Class<*>) =
    try {
        (getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager)
            ?.getRunningServices(Int.MAX_VALUE)
            ?.any { it.service.className == serviceClass.name } ?: false
    } catch (e: Exception) {
        false
    }

fun Context.openFile(file: File) {
    Intent(Intent.ACTION_VIEW).apply {
        flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        addCategory(Intent.CATEGORY_DEFAULT)
        val uri = FileProvider.getUriForFile(this@openFile, BuildConfig.APPLICATION_ID + ".provider", file)
        val mimeType = getMimeType(file)
        mimeType?.let {
            setDataAndType(uri, it)
            startActivity(this)
        }
    }
}

fun getMimeType(file: File): String? {
    val extension = file.extension
    return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
}

fun Lecture.toBundle() =
    Bundle().apply {
        putLong(LECTURE_KEY_ID, id)
        putString(LECTURE_KEY_TITLE, title)
        putString(LECTURE_KEY_DESCRIPTION, description)
        putString(LECTURE_KEY_DATE, date)
        putString(LECTURE_KEY_PLACE, place)
        putLong(LECTURE_KEY_DURATION, durationMillis)
        putString(LECTURE_KEY_FILE_URL, fileUrl)
        putString(LECTURE_KEY_REMOTE_URL, remoteUrl)
        putBoolean(LECTURE_KEY_IS_FAVORITE, isFavorite)
        downloadProgress?.let { putInt(LECTURE_KEY_DOWNLOAD_PROGRESS, it) }
    }

fun Bundle.toLecture(): Lecture? {
    val id = getLongOrNull(LECTURE_KEY_ID) ?: return null
    val title = getStringOrNull(LECTURE_KEY_TITLE) ?: return null
    val description = getStringOrNull(LECTURE_KEY_DESCRIPTION)
    val date = getStringOrNull(LECTURE_KEY_DATE) ?: return null
    val place = getStringOrNull(LECTURE_KEY_PLACE) ?: return null
    val durationMillis = getLongOrNull(LECTURE_KEY_DURATION) ?: return null
    val fileUrl = getStringOrNull(LECTURE_KEY_FILE_URL)
    val remoteUrl = getStringOrNull(LECTURE_KEY_REMOTE_URL) ?: return null
    val isFavorite = getBooleanOrNull(LECTURE_KEY_IS_FAVORITE) ?: return null
    val downloadProgress = getIntOrNull(LECTURE_KEY_DOWNLOAD_PROGRESS)

    return Lecture(
        id = id,
        title = title,
        description = description,
        date = date,
        place = place,
        durationMillis = durationMillis,
        fileUrl = fileUrl,
        remoteUrl = remoteUrl,
        isFavorite = isFavorite,
        downloadProgress = downloadProgress
    )
}

fun Bundle.getIntOrNull(key: String) =
    if (containsKey(key)) getInt(key) else null

fun Bundle.getLongOrNull(key: String) =
    if (containsKey(key)) getLong(key) else null

fun Bundle.getBooleanOrNull(key: String) =
    if (containsKey(key)) getBoolean(key) else null

fun Bundle.getStringOrNull(key: String) = getString(key)