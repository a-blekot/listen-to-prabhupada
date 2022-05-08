package com.prabhupadalectures.lectures.data

import com.prabhupadalectures.common.network_api.ApiModel
import com.prabhupadalectures.common.network_api.Routes
import com.prabhupadalectures.common.network_api.lectures.*
import com.prabhupadalectures.lectures.data.lectures.*

fun lectures(apiModel: ApiModel): List<Lecture> =
    apiModel.results.files.map { lecture(it) }

private fun lecture(apiModel: LectureApiModel) =
    Lecture(
        id = apiModel.id,
        title = apiModel.title,
        description = apiModel.description,
        date = apiModel.date,
        place = apiModel.place.name,
        durationMillis = apiModel.duration.parseDuration(),
        remoteUrl = "${Routes.BASE_URL}${apiModel.url}"
    )

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
        resolution = apiModel.resolution
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
