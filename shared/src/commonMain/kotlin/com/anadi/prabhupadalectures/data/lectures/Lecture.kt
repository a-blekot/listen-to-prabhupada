package com.anadi.prabhupadalectures.data.lectures

data class Lecture(
    val id: Long = 0L,
    val slug: String = "",

    val date: String = "",
    val place: String = "",
    val title: String = "",
    val description: String? = null,
    val quotes: List<QuoteModel> = emptyList(),
    val categories: List<String> = emptyList(),
    val participants: List<Participant> = emptyList(),

    val fileInfo: FileInfo = FileInfo(),

    val state: String = "",
    val tags: List<Tag> = emptyList(),
    val event: Event? = null,
    val period: Period? = null,
    val resolution: String? = null,
    val isPlaying: Boolean = false,
    val isFavorite: Boolean = false,
    val isDownloaded: Boolean = false,
) {
    val displayedTitle
        get() = title

    val displayedSubTitle
        get() = "$date, $place"

    val displayedDescription
        get() = description
}
