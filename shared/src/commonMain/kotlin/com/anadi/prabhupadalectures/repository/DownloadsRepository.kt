package com.anadi.prabhupadalectures.repository

import com.anadi.prabhupadalectures.data.Database
import com.anadi.prabhupadalectures.data.lectures.Lecture
import com.anadi.prabhupadalectures.data.lectures.file
import com.anadi.prabhupadalectures.network.api.*
import com.anadi.prabhupadalectures.writeChannel
import io.github.aakira.napier.Napier
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.io.Serializable
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicBoolean

interface DownloadsRepository {
    val hasActiveDownloads: Boolean

    fun download(lecture: Lecture)
    fun checkPendingDownloads()
    fun observeDownload(): SharedFlow<DownloadState>
}

class DownloadsRepositoryImpl(
    private val db: Database,
    private val api: PrabhupadaApi
) : DownloadsRepository, Serializable {

    private val downloadFlow = MutableSharedFlow<DownloadState>()
    private val isProcessingDownload = AtomicBoolean(false)
    private val ioScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val queue = ConcurrentLinkedQueue<Lecture>()

    override val hasActiveDownloads
        get() = isProcessingDownload.get() || !queue.isEmpty()

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
                fileUrl = lecture.file.path,
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
        if (!isProcessingDownload.get()) {
            ioScope.launch {
                setDownloadsState(Idle)
            }
            queue.peek()
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
            state is Success && lecture.file.exists() -> {
                db.insertCachedLecture(
                    lecture.copy(downloadProgress = FULL_PROGRESS)
                )
            }
            else -> db.deleteFromDownloadsOnly(lecture)
        }

        queue.poll()
        isProcessingDownload.set(false)

        setDownloadsState(state.apply { this.lecture = lecture })
        checkPendingDownloads()
    }

    private suspend fun setDownloadsState(value: DownloadState) {
        value.print("DownloadsRepository")
        downloadFlow.emit(value)
    }

    private fun printQueue() {
        Napier.d("Queue", tag = "DownloadsRepository")
        Napier.d("isProcessingDownload = ${isProcessingDownload.get()}", tag = "DownloadsRepository")
        if (queue.isEmpty()) {
            Napier.d("Is empty!", tag = "DownloadsRepository")
        }
        queue.map { "id = ${it.id}, title = ${it.title}" }
            .forEach {
                Napier.d(it, tag = "DownloadsRepository")
            }
    }
}