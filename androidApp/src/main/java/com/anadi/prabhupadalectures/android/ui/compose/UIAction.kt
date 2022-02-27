package com.anadi.prabhupadalectures.android.ui.compose

import com.anadi.prabhupadalectures.data.lectures.Lecture
import com.anadi.prabhupadalectures.datamodel.QueryParam

sealed class UIAction
data class Play(val lecture: Lecture, val isPlaying: Boolean): UIAction()
data class Pause(val lecture: Lecture, val isPlaying: Boolean): UIAction()
object Next: UIAction()
object Prev: UIAction()
object SeekForward: UIAction()
object SeekBack: UIAction()

data class Option(val queryParam: QueryParam): UIAction()
data class Favorite(val lectureId: Long, val isFavorite: Boolean): UIAction()