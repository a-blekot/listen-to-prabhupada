package com.anadi.prabhupadalectures.repository

import com.anadi.prabhupadalectures.data.PAGE_QUERY_KEY
import com.russhwolf.settings.Settings

val settings = Settings()

private const val FILTERS_SEPARATOR = ", "
private const val KEY_VALUE_SEPARATOR = "::"
private const val KEY_QUERY_PARAMS = "KEY_QUERY_PARAMS"
private const val KEY_PAGE = "KEY_PAGE"
private const val KEY_NOTIFICATION_ID = "KEY_NOTIFICATION_ID"

const val FIRST_PAGE = 1
const val DOWNLOAD_NOTIFICATION_ID = 16198

fun Settings.savePage(page: Int) =
    putInt(KEY_PAGE, page)

fun Settings.getPage(): Int =
    getInt(KEY_PAGE, defaultValue = FIRST_PAGE)

fun Settings.saveQueryParams(queryParams: HashMap<String, Any>) =
    saveQueryParams(queryParams.toQueryParamsStringWithoutPage())

fun Settings.saveQueryParams(queryParams: String) =
    putString(KEY_QUERY_PARAMS, queryParams)

fun Settings.getQueryParams() =
    getStringOrNull(KEY_QUERY_PARAMS)?.ifEmpty { null }.toQueryParamsMap()

fun Settings.getNextNotificationId(): Int {
    val notificationId = getIntOrNull(KEY_NOTIFICATION_ID) ?: DOWNLOAD_NOTIFICATION_ID + 1
    putInt(KEY_NOTIFICATION_ID, notificationId + 1)
    return notificationId
}

fun HashMap<String, Any>.addPage(page: Int) =
    apply {
        put(PAGE_QUERY_KEY, page.coerceAtLeast(FIRST_PAGE))
    }

fun HashMap<String, Any>.toDatabaseIdentifier() =
    toQueryParamsStringWithoutPage().hashCode().toLong()

fun HashMap<String, Any>.toQueryParamsStringWithoutPage() =
    entries
        .filter { it.key != PAGE_QUERY_KEY }
        .joinToString(separator = FILTERS_SEPARATOR) {
            "${it.key}$KEY_VALUE_SEPARATOR${it.value}"
        }

private fun String?.toQueryParamsMap(): HashMap<String, Any> {
    val result = HashMap<String, Any>()

    this?.split(FILTERS_SEPARATOR)
        ?.forEach { filter ->
            val (name, value) = filter.split(KEY_VALUE_SEPARATOR)
            result[name] = value
        }

    return result
}
