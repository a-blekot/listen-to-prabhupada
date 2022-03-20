package com.anadi.prabhupadalectures.network.api

import com.anadi.prabhupadalectures.data.lectures.Lecture

const val ZERO_PROGRESS = 0
const val FULL_PROGRESS = 100

sealed class DownloadState(var lecture: Lecture? = null)

object Idle : DownloadState()
object Success : DownloadState()
object AllDownloadsCompleted: DownloadState()
data class Error(val message: String, val t: Throwable? = null) : DownloadState()
data class Progress(val progress: Int): DownloadState()