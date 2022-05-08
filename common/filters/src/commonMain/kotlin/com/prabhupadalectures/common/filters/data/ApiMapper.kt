package com.prabhupadalectures.common.filters.data

import com.prabhupadalectures.common.network_api.ApiModel
import com.prabhupadalectures.common.network_api.filters.FilterApiModel
import com.prabhupadalectures.common.network_api.filters.OptionApiModel

private const val LECTURES_PER_PAGE = 6

fun totalLecturesCount(apiModel: ApiModel) =
    apiModel.count

fun pagesCount(apiModel: ApiModel) =
    apiModel.count / LECTURES_PER_PAGE + if (apiModel.count % LECTURES_PER_PAGE == 0) 0 else 1

fun filters(apiModel: ApiModel): List<Filter> =
    apiModel.results.filters.map { filter(it) }

private fun filter(apiModel: FilterApiModel) =
    Filter(
        name = apiModel.name,
        title = apiModel.title,
        parent = apiModel.parent,
        options = apiModel.options.map { option(it) }
    )

private fun option(apiModel: OptionApiModel) =
    Option(
        value = apiModel.value,
        text = apiModel.text,
        isSelected = apiModel.selected
    )
