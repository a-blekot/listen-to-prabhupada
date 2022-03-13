package com.anadi.prabhupadalectures.android.ui.compose

import com.anadi.prabhupadalectures.datamodel.QueryParam

sealed class UIAction
data class Play(val lectureId: Long): UIAction()
object Pause: UIAction()
object Next: UIAction()
object Prev: UIAction()
object SeekForward: UIAction()
object SeekBack: UIAction()
data class SeekTo(val timeMs: Long): UIAction()
object SliderReleased: UIAction()

data class Option(val queryParam: QueryParam): UIAction()
data class Favorite(val lectureId: Long, val isFavorite: Boolean): UIAction()