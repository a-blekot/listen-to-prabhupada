package com.prabhupadalectures.common.network_api.lectures

import com.prabhupadalectures.common.network_api.filters.FilterApiModel
import kotlinx.serialization.Serializable

@Serializable
data class ResultsApiModel(
    val files: List<LectureApiModel>,
    val filters: List<FilterApiModel>
)