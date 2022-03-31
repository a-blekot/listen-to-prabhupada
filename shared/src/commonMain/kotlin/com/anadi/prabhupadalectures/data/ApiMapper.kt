package com.anadi.prabhupadalectures.data

import com.anadi.prabhupadalectures.data.filters.Filter
import com.anadi.prabhupadalectures.data.filters.Option
import com.anadi.prabhupadalectures.data.lectures.*
import com.anadi.prabhupadalectures.network.Routes
import com.anadi.prabhupadalectures.network.api.ApiModel
import com.anadi.prabhupadalectures.network.api.filters.FilterApiModel
import com.anadi.prabhupadalectures.network.api.filters.OptionApiModel
import com.anadi.prabhupadalectures.network.api.lectures.*

object ApiMapper {
    fun pagination(apiModel: ApiModel) =
        Pagination(apiModel)

    fun lectures(apiModel: ApiModel): List<Lecture> =
        apiModel.results.files.map { lecture(it) }

    fun filters(apiModel: ApiModel): List<Filter> =
        apiModel.results.filters.map { filter(it) }

    fun lecture(apiModel: LectureApiModel) =
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

    fun quote(apiModel: QuoteApiModel) =
        QuoteModel(
            scripture = apiModel.scripture.title,
            canto = apiModel.canto?.title,
            chapter = apiModel.chapter,
            verse = apiModel.verse
        )

    fun participant(apiModel: ParticipantApiModel) =
        Participant(
            name = apiModel.name,
            photo = apiModel.photo,
            description = apiModel.description,
        )

    fun fileInfo(apiModel: LectureApiModel) =
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

    fun fileType(apiModel: FileTypeApiModel) =
        FileType(
            title = apiModel.title,
            mimetypes = apiModel.mimetypes
        )

    fun tag(apiModel: TagApiModel) =
        Tag(
            id = apiModel.id,
            title = apiModel.title,
            aliases = apiModel.aliases,
        )

    fun event(apiModel: EventApiModel) =
        Event(
            id = apiModel.id,
            name = apiModel.name,
        )

    fun period(apiModel: PeriodApiModel) =
        Period(
            id = apiModel.id,
            title = apiModel.title,
        )

    fun filter(apiModel: FilterApiModel) =
        Filter(
            name = apiModel.name,
            title = apiModel.title,
            parent = apiModel.parent,
            options = apiModel.options.map { option(it) }
        )

    fun option(apiModel: OptionApiModel) =
        Option(
            value = apiModel.value,
            text = apiModel.text,
            isSelected = apiModel.selected
        )
}
