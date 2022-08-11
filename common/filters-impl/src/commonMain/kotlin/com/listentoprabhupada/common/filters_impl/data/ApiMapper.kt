package com.listentoprabhupada.common.filters_impl.data

import com.listentoprabhupada.common.filters_api.Filter
import com.listentoprabhupada.common.filters_api.Option
import com.listentoprabhupada.common.network_api.ApiModel
import com.listentoprabhupada.common.network_api.FILE_TYPE_QUERY_KEY
import com.listentoprabhupada.common.network_api.filters.FilterApiModel
import com.listentoprabhupada.common.network_api.filters.OptionApiModel

private const val LECTURES_PER_PAGE = 10

fun totalLecturesCount(apiModel: ApiModel) =
    apiModel.count

fun pagesCount(apiModel: ApiModel) =
    apiModel.count / LECTURES_PER_PAGE + if (apiModel.count % LECTURES_PER_PAGE == 0) 0 else 1

fun filters(apiModel: ApiModel): List<Filter> =
    apiModel.results.filters
        .filter { it.name != FILE_TYPE_QUERY_KEY}
        .map { filter(it) }

private fun filter(apiModel: FilterApiModel) =
    Filter(
        name = apiModel.name,
        title = title(apiModel),
        parent = apiModel.parent,
        options = apiModel.options.map { option(it) }
    )

private fun option(apiModel: OptionApiModel) =
    Option(
        value = apiModel.value,
        text = apiModel.text,
        isSelected = apiModel.selected
    )

// TODO should be implemented on backend
private fun title(apiModel: FilterApiModel) =
    when (apiModel.title) {
        "Category" -> "Категории"
        "Scripture" -> "Писания"
        "Country" -> "Страна"
        "Locality" -> "Локация"
        "Book" -> "Книга"
        "Chapter" -> "Глава"
        "Verse" -> "Стих"
        else -> apiModel.title
    }
