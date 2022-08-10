package com.listentoprabhupada.common.results_impl.store

import com.listentoprabhupada.common.data.Lecture

sealed interface ResultsLabel {
    data class LecturesLoaded(val lectures: List<Lecture>) : ResultsLabel
}