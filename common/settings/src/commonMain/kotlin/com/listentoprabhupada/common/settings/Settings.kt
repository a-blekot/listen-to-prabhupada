package com.listentoprabhupada.common.settings

import com.russhwolf.settings.Settings
import kotlin.native.concurrent.SharedImmutable

@SharedImmutable
private const val FILTERS_SEPARATOR_V1 = ", "
@SharedImmutable
private const val FILTERS_SEPARATOR_V2 = "&"
@SharedImmutable
private const val KEY_VALUE_SEPARATOR_V1 = "::"
@SharedImmutable
private const val KEY_VALUE_SEPARATOR_V2 = "="

@SharedImmutable
internal const val LATEST_FILTERS_SEPARATOR = FILTERS_SEPARATOR_V2
@SharedImmutable
internal const val LATEST_KEY_VALUE_SEPARATOR = KEY_VALUE_SEPARATOR_V2

@SharedImmutable
const val PAGE_QUERY_KEY = "page"
@SharedImmutable
const val FIRST_PAGE = 1

val settings = Settings()

fun separatorsForString(s: String?): Pair<String, String>? =
    when {
        s == null -> null
        s.contains(KEY_VALUE_SEPARATOR_V2) -> Pair(FILTERS_SEPARATOR_V2, KEY_VALUE_SEPARATOR_V2)
        s.contains(KEY_VALUE_SEPARATOR_V1) -> Pair(FILTERS_SEPARATOR_V1, KEY_VALUE_SEPARATOR_V1)
        else -> null
    }
