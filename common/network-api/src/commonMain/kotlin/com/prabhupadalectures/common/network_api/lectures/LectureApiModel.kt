package com.listentoprabhupada.common.network_api.lectures

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LectureApiModel(
    val id: Long,
    val slug: String, // 01071972-san-diego-obrashchenie-etot-mir-okoldovan-seksom

    val date: String, // 1971-06-27
    val place: PlaceApiModel,
    val title: String,
    val description: String?,
    val quotes: List<QuoteApiModel>,
    val categories: List<CategoryApiModel>,
    val participants: List<ParticipantApiModel>,

    @SerialName("t")
    val fileType: FileTypeApiModel,
    @SerialName("mime_type")
    val mimeType: String, // audio/mpeg
    @SerialName("audio_quality")
    val audioQuality: Long, // 80000
    @SerialName("video_quality")
    val videoQuality: Long?, // 80000

    @SerialName("f")
    val url: String, // /media/archive/ShrilaPrabhupada/1966/00%20%D0%91.%D0%93..00.06-(08.1966%2C%20%D0%9D%D1%8C%D1%8E%20%D0%98%CC%86%D0%BE%D1%80%D0%BA)%20(%D1%82%D0%B5%D0%BA%D1%81%D1%82%20%D0%B2%20ID3%20%D1%82%D0%B5%D0%B3%D0%B5).mp3
    @SerialName("file_md5")
    val md5: String, // ddb407e1a37daba9234093b0eb16a95a
    @SerialName("file_size")
    val size: Long, // 1_931_737 bytes
    val duration: String, // 00:03:09.072000

    // enum {
    // 'new', 'processed', 'published', 'archived', 'hidden', 'deleted'
    // }
    // we need only published
    val state: String,
    val tags: List<TagApiModel>,
    val event: EventApiModel?,
    val period: PeriodApiModel?,
//    val resolution: String?
)