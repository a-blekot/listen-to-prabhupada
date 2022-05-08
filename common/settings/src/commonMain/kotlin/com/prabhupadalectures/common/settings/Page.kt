package com.prabhupadalectures.common.settings

import com.russhwolf.settings.Settings

private const val KEY_PAGE = "KEY_PAGE"

fun Settings.savePage(page: Int) =
    putInt(KEY_PAGE, page)

fun Settings.getPage(): Int =
    getInt(KEY_PAGE, defaultValue = FIRST_PAGE)

fun HashMap<String, Any>.addPage(page: Int) =
    apply {
        put(PAGE_QUERY_KEY, page.coerceAtLeast(FIRST_PAGE))
    }