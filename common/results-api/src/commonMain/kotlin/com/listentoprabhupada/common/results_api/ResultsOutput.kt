package com.listentoprabhupada.common.results_api

import com.listentoprabhupada.common.data.Lecture

sealed interface ResultsOutput {
    object Pause : ResultsOutput
    data class Play(val lectureId: Long) : ResultsOutput
    data class UpdatePlaylist(val lectures: List<Lecture>) : ResultsOutput
}
