package com.prabhupadalectures.android.util

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import com.prabhupadalectures.android.BuildConfig
import java.io.File

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

fun Bundle.getIntOrNull(key: String) =
    if (containsKey(key)) getInt(key) else null

fun Bundle.getLongOrNull(key: String) =
    if (containsKey(key)) getLong(key) else null

fun Bundle.getBooleanOrNull(key: String) =
    if (containsKey(key)) getBoolean(key) else null

fun Bundle.getStringOrNull(key: String) = getString(key)

fun <T> selector(positive: T, negative: T, condition: Boolean): T =
    if (condition) positive else negative