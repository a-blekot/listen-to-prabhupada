package com.anadi.prabhupadalectures.repository

import co.touchlab.stately.collections.IsoArrayDeque
import com.anadi.prabhupadalectures.data.Database
import com.anadi.prabhupadalectures.data.lectures.Lecture
import com.anadi.prabhupadalectures.data.lectures.exists
import com.anadi.prabhupadalectures.data.lectures.filePath
import com.anadi.prabhupadalectures.network.api.*
import com.anadi.prabhupadalectures.writeChannel
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

interface DownloadsRepository {
    val hasActiveDownloads: Boolean

    fun download(lecture: Lecture)
    fun checkPendingDownloads()
    fun observeDownload(): SharedFlow<DownloadState>
}

@OptIn(ExperimentalStdlibApi::class)
class DownloadsRepositoryImpl(
    private val db: Database,
    private val api: PrabhupadaApi
) : DownloadsRepository {

    private val downloadFlow = MutableSharedFlow<DownloadState>()
    private val ioScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val queue = IsoArrayDeque<Lecture>()

    override val hasActiveDownloads
        get() = !queue.isEmpty()

    init {
        val list = db.selectAllDownloads()
        queue.addAll(list.filter { it.downloadProgress != FULL_PROGRESS })

        Napier.d("init", tag = "DownloadsRepository")
        Napier.d(
            "queue.addAll.count = ${list.filter { it.downloadProgress != FULL_PROGRESS }.size}",
            tag = "DownloadsRepository"
        )
        checkPendingDownloads()
    }

    override fun observeDownload() = downloadFlow.asSharedFlow()

    override fun download(lecture: Lecture) =
        lecture.run {
            Napier.d("download $lecture", tag = "DownloadsRepository")

            when {
                remoteUrl.isBlank() -> return
                queue.any { it.id == id } -> return
                db.selectCachedLecture(id)?.isDownloaded == true -> return
            }

            val temp = copy(
                fileUrl = lecture.filePath.toString(),
                downloadProgress = ZERO_PROGRESS
            )

            Napier.d("lecture is ok to download", tag = "DownloadsRepository")

            queue.add(temp)
            db.insertCachedLecture(temp)
            checkPendingDownloads()
        }

    override fun checkPendingDownloads() {
        Napier.d("checkPendingDownloads", tag = "DownloadsRepository")
        printQueue()
        if (!queue.isEmpty()) {
            ioScope.launch {
                setDownloadsState(Idle)
            }
            queue.firstOrNull()
                ?.let { startDownloadingTask(it) }
        }
    }

    private fun startDownloadingTask(lecture: Lecture) =
        ioScope.launch {
            Napier.d("startDownloadingTask $lecture", tag = "DownloadsRepository")
            printQueue()
            api.downloadFile(lecture.writeChannel(), lecture.remoteUrl)
                .collect { state ->
                    when (state) {
                        is Success -> {
                            handleTaskCompleted(lecture, state)
                        }
                        is Error -> {
                            Napier.e("DownloadError $lecture", state.t, "DownloadsRepository")
                            handleTaskCompleted(lecture, state)
                        }
                        else -> {
                            setDownloadsState(state)
                        }
                    }
                }
        }

    private suspend fun handleTaskCompleted(lecture: Lecture, state: DownloadState) {
        Napier.d("handleTaskCompleted $state", tag = "DownloadsRepository")

        when {
            state is Success && lecture.exists() -> {
                db.insertCachedLecture(
                    lecture.copy(downloadProgress = FULL_PROGRESS)
                )
            }
            else -> db.deleteFromDownloadsOnly(lecture)
        }

        queue.removeFirstOrNull()

        setDownloadsState(state.apply { this.lecture = lecture })
        checkPendingDownloads()
    }

    private suspend fun setDownloadsState(value: DownloadState) {
        value.print("DownloadsRepository")
        downloadFlow.emit(value)
    }

    private fun printQueue() {
        Napier.d("Queue", tag = "DownloadsRepository")
        if (queue.isEmpty()) {
            Napier.d("Is empty!", tag = "DownloadsRepository")
        }
        queue.map { "id = ${it.id}, title = ${it.title}" }
            .forEach {
                Napier.d(it, tag = "DownloadsRepository")
            }
    }
}