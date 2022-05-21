package com.prabhupadalectures.common.settings

import com.russhwolf.settings.Settings
import io.github.aakira.napier.Napier
import kotlin.native.concurrent.SharedImmutable

@SharedImmutable
private const val KEY_FILTERS = "KEY_FILTERS"

fun Settings.saveFilters(queryParams: HashMap<String, Any>) =
    saveFilters(queryParams.toQueryParamsStringWithoutPage())

fun Settings.saveFilters(filters: String) =
    putString(KEY_FILTERS, filters)

fun Settings.getQueryParams() =
    getFiltersAsString().toQueryParamsMap().addPage(FIRST_PAGE)

fun Settings.getFilters() =
    getFiltersAsString().toQueryParamsMap()

fun Settings.getFiltersAsString() =
    getStringOrNull(KEY_FILTERS)?.ifEmpty { null }

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