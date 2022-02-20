package com.anadi.prabhupadalectures.android.ui.compose

import com.anadi.prabhupadalectures.data.lectures.Lecture
import com.anadi.prabhupadalectures.datamodel.QueryParam

sealed class UIAction
data class PlayClick(val lecture: Lecture, val isPlaying: Boolean): UIAction()
data class OptionClick(val queryParam: QueryParam): UIAction()
data class FavoriteClick(val lectureId: Long, val isFavorite: Boolean): UIAction()