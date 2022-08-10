package com.listentoprabhupada.common.results_impl.data

import com.listentoprabhupada.common.results_api.LECTURES_PER_PAGE
import com.listentoprabhupada.common.data.Lecture
import com.listentoprabhupada.common.results_api.Pagination
import com.listentoprabhupada.common.network_api.ApiModel
import com.listentoprabhupada.common.network_api.Routes
import com.listentoprabhupada.common.network_api.lectures.*
import com.listentoprabhupada.common.settings.FIRST_PAGE
import io.github.aakira.napier.Napier

fun lectures(apiModel: ApiModel): List<Lecture> =
    apiModel.results.files.map { lecture(it) }

fun pagination(apiModel: ApiModel) =
    Pagination(
        prev = apiModel.prevPage,
        curr = currentPage(apiModel),
        next = apiModel.nextPage,
        total = totalPages(apiModel.count)
    )

private fun currentPage(apiModel: ApiModel) =
    apiModel.prevPage?.let { it + 1 }
        ?: apiModel.nextPage?.let { it - 1 }
        ?: FIRST_PAGE

private fun totalPages(totalLectures: Int) =
    totalLectures / LECTURES_PER_PAGE + if (totalLectures % LECTURES_PER_PAGE == 0) 0 else 1

private fun lecture(apiModel: LectureApiModel, idExtra: Long = 0) =
    Lecture(
        id = apiModel.id + idExtra,
        title = title(apiModel),
        description = apiModel.description,
        date = apiModel.date,
        place = apiModel.place.name,
        durationMillis = apiModel.duration.parseDuration(),
        remoteUrl = "${Routes.BASE_URL}${apiModel.url}"
    )

private fun title(apiModel: LectureApiModel) =
    apiModel.run {
        when {
            quotes.isEmpty() -> title
            else -> title(quotes) ?: title
        }
    }

private fun title(quotes: List<QuoteApiModel>) =

    when (quotes.size) {
        0 -> null
        1 -> title(quotes.first())
        else -> title(quotes.first(), quotes.last())
    }

private fun title(quote: QuoteApiModel) =
    "${scripture(quote)} ${number(quote)}"

private fun title(first: QuoteApiModel, last: QuoteApiModel) =
    "${scripture(first)} ${number(first, last)}"

private fun scripture(quote: QuoteApiModel) =
    quote.scripture.title

private fun number(first: QuoteApiModel, last: QuoteApiModel) =
    when {
        canto(first) == canto(last) && first.chapter == last.chapter -> {
            listOfNotNull(canto(first), first.chapter, "${first.verse}-${last.verse}").joinToString(separator = ".")
        }

        else -> "${number(first)}-${number(last)}"
    }

private fun number(quote: QuoteApiModel) =
    quote.run {
        listOfNotNull(canto?.title, chapter, verse).joinToString(separator = ".")
    }

private fun canto(quote: QuoteApiModel) =
    quote.canto?.title

// 00:03:09.072000
private fun String.parseDuration() =
    try {
        val arr = split(":")
        val hours = arr.getOrNull(0)?.toLong() ?: 0L
        val minutes = arr.getOrNull(1)?.toLong() ?: 0L
        val seconds = arr.getOrNull(2)?.toFloat() ?: 0F

        hours * 3_600_000 + minutes * 60_000 + (seconds * 1000).toLong()
    } catch (e: NumberFormatException) {
        Napier.e("failed to compute durationMillis", e)
        0L
    }