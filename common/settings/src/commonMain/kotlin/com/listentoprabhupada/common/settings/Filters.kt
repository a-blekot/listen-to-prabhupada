package com.listentoprabhupada.common.settings

import com.russhwolf.settings.Settings
import kotlin.native.concurrent.SharedImmutable

@SharedImmutable
private const val KEY_ALL_FILTER_OPTIONS = "KEY_ALL_FILTER_OPTIONS"
@SharedImmutable
private const val KEY_PAGES_COUNT = "KEY_PAGES_COUNT"
@SharedImmutable
private const val KEY_TOTAL_LECTURES_COUNT = "KEY_TOTAL_LECTURES_COUNT"

fun HashMap<String, Any>.toDatabaseIdentifier() =
    toQueryParamsStringWithoutPage().hashCode().toLong()

fun Settings.saveFilterOptions(allFilterOptions: String) =
    putString(KEY_ALL_FILTER_OPTIONS, allFilterOptions)

fun Settings.getFilterOptions() =
    getString(KEY_ALL_FILTER_OPTIONS)

fun Settings.savePagesCount(pages: Int) =
    putInt(KEY_PAGES_COUNT, pages)

fun Settings.getPagesCount() =
    getInt(KEY_PAGES_COUNT)

fun Settings.saveTotalLecturesCount(total: Int) =
    putInt(KEY_TOTAL_LECTURES_COUNT, total)

fun Settings.getTotalLecturesCount() =
    getInt(KEY_TOTAL_LECTURES_COUNT)