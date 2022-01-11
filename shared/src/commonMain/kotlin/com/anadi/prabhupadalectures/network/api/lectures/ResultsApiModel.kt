package com.anadi.prabhupadalectures.network.api.lectures

import com.anadi.prabhupadalectures.network.api.filters.FilterApiModel
import kotlinx.serialization.Serializable

@Serializable
data class ResultsApiModel(
    val files: List<LectureApiModel>,
    val filters: List<FilterApiModel>
)