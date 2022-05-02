package com.prabhupadalectures.lectures.repository

import com.prabhupadalectures.common.network_api.PAGE_QUERY_KEY
import com.russhwolf.settings.Settings
import io.github.aakira.napier.Napier

val settings = Settings()

const val FILTERS_SEPARATOR_V1 = ", "
const val FILTERS_SEPARATOR_V2 = "&"
const val LATEST_FILTERS_SEPARATOR = FILTERS_SEPARATOR_V2
const val KEY_VALUE_SEPARATOR_V1 = "::"
const val KEY_VALUE_SEPARATOR_V2 = "="
const val LATEST_KEY_VALUE_SEPARATOR = KEY_VALUE_SEPARATOR_V2
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

fun Settings.getQueryParamsAsString() =
    getStringOrNull(KEY_QUERY_PARAMS)?.ifEmpty { null }

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

fun HashMap<String, Any>.toQueryParamsString() =
    entries
        .joinToString(separator = LATEST_FILTERS_SEPARATOR) {
            "${it.key}$LATEST_KEY_VALUE_SEPARATOR${it.value}"
        }

fun HashMap<String, Any>.toQueryParamsStringWithoutPage() =
    entries
        .filter { it.key != PAGE_QUERY_KEY }
        .joinToString(separator = LATEST_FILTERS_SEPARATOR) {
            "${it.key}$LATEST_KEY_VALUE_SEPARATOR${it.value}"
        }

fun String?.toQueryParamsMap(): HashMap<String, Any> {
    val result = HashMap<String, Any>()

    val (filtersSeparator, keyValueSeparator) = separatorsForString(this) ?: return result

    Napier.d("toQueryParamsMap $this", tag = "Settings")
    this?.split(filtersSeparator)
        ?.forEach { filter ->
            val list = filter.split(keyValueSeparator)
            if (list.size == 2) {
                result[list[0]] = list[1]
            }
        }

    return result
}

fun separatorsForString(s: String?): Pair<String, String>? =
    when {
        s == null -> null
        s.contains(KEY_VALUE_SEPARATOR_V2) -> Pair(FILTERS_SEPARATOR_V2, KEY_VALUE_SEPARATOR_V2)
        s.contains(KEY_VALUE_SEPARATOR_V1) -> Pair(FILTERS_SEPARATOR_V1, KEY_VALUE_SEPARATOR_V1)
        else -> null
    }
