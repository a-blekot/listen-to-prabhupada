package com.prabhupadalectures.common.lectures_impl.data

import com.prabhupadalectures.common.lectures_api.LECTURES_PER_PAGE
import com.prabhupadalectures.common.utils.Lecture
import com.prabhupadalectures.common.lectures_api.Pagination
import com.prabhupadalectures.common.lectures_impl.data.lectures.*
import com.prabhupadalectures.common.network_api.ApiModel
import com.prabhupadalectures.common.network_api.Routes
import com.prabhupadalectures.common.network_api.lectures.*
import com.prabhupadalectures.common.settings.FIRST_PAGE
import io.github.aakira.napier.Napier

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

fun lectures(apiModel: ApiModel): List<Lecture> =
    apiModel.results.files.map { lecture(it) }

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

fun lectureFullModel(apiModel: LectureApiModel) =
    LectureFullModel(
        id = apiModel.id,
        slug = apiModel.slug,
        date = apiModel.date,
        place = apiModel.place.name,
        title = apiModel.title,
        description = apiModel.description,
        quotes = apiModel.quotes.map { quote(it) },
        categories = apiModel.categories.map { it.title },
        participants = apiModel.participants.map { participant(it) },
        fileInfo = fileInfo(apiModel),

        state = apiModel.state,
        tags = apiModel.tags.map { tag(it) },
        event = apiModel.event?.let { event(it) },
        period = apiModel.period?.let { period(it) },
//        resolution = apiModel.resolution
    )

private fun quote(apiModel: QuoteApiModel) =
    QuoteModel(
        scripture = apiModel.scripture.title,
        canto = apiModel.canto?.title,
        chapter = apiModel.chapter,
        verse = apiModel.verse
    )

private fun participant(apiModel: ParticipantApiModel) =
    Participant(
        name = apiModel.name,
        photo = apiModel.photo,
        description = apiModel.description,
    )

private fun fileInfo(apiModel: LectureApiModel) =
    FileInfo(
        fileType = fileType(apiModel.fileType),
        mimeType = apiModel.mimeType,
        audioQuality = apiModel.audioQuality,
        videoQuality = apiModel.videoQuality,
        url = apiModel.url,
        md5 = apiModel.md5,
        size = apiModel.size,
        duration = apiModel.duration,
    )

private fun fileType(apiModel: FileTypeApiModel) =
    FileType(
        title = apiModel.title,
        mimetypes = apiModel.mimetypes
    )

private fun tag(apiModel: TagApiModel) =
    Tag(
        id = apiModel.id,
        title = apiModel.title,
        aliases = apiModel.aliases,
    )

private fun event(apiModel: EventApiModel) =
    Event(
        id = apiModel.id,
        name = apiModel.name,
    )

private fun period(apiModel: PeriodApiModel) =
    Period(
        id = apiModel.id,
        title = apiModel.title,
    )
