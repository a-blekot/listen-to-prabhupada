package com.anadi.prabhupadalectures.network.api

import com.anadi.prabhupadalectures.data.lectures.Lecture
import io.github.aakira.napier.Napier

const val ZERO_PROGRESS = 0
const val FULL_PROGRESS = 100

sealed class DownloadState(var lecture: Lecture? = null)

object Idle : DownloadState()
object Success : DownloadState()
data class Error(val message: String, val t: Throwable? = null) : DownloadState()
data class Progress(val progress: Int): DownloadState()

fun DownloadState.print(tag: String?) {
    when (this) {
        is Progress -> {
            if (progress % 20 == 0) {
                Napier.d("downloadState = $this", tag = tag)
            }
        }
        else -> {
            Napier.d("downloadState = $this", tag = tag)
        }
    }
}

