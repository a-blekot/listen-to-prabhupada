package com.listentoprabhupada.common.results_impl.repository

import com.listentoprabhupada.common.data.Lecture
import com.listentoprabhupada.common.database.Database
import com.listentoprabhupada.common.database.isDownloaded
import com.listentoprabhupada.common.network_api.*
import com.listentoprabhupada.common.results_impl.data.exists
import com.listentoprabhupada.common.results_impl.data.filePath
import com.listentoprabhupada.common.results_impl.writeChannel
import com.listentoprabhupada.common.utils.dbEntity
import com.listentoprabhupada.common.utils.mapped
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

interface DownloadsRepository {
    val hasActiveDownloads: Boolean

    fun download(lecture: Lecture)
    fun observeDownload(): SharedFlow<DownloadState>
}

class DownloadsRepositoryImpl(
    private val db: Database,
    private val api: PrabhupadaApi
) : DownloadsRepository {

    private val downloadFlow = MutableSharedFlow<DownloadState>()
    private val ioScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val queue = ArrayDeque<Lecture>()

    override val hasActiveDownloads
        get() = !queue.isEmpty()

    init {
        val list = db.selectAllDownloads().mapped()
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
                db.selectLecture(id)?.isDownloaded == true -> return
            }

            val temp = copy(
                fileUrl = lecture.filePath.toString(),
                downloadProgress = ZERO_PROGRESS
            )

            Napier.d("lecture is ok to download", tag = "DownloadsRepository")

            queue.add(temp)
            db.insertLecture(temp.dbEntity())
            checkPendingDownloads()
        }

    private fun checkPendingDownloads() {
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
                db.insertLecture(
                    lecture.copy(downloadProgress = FULL_PROGRESS).dbEntity()
                )
            }
            else -> db.deleteFromDownloadsOnly(lecture.dbEntity())
        }

        queue.removeFirstOrNull()

        setDownloadsState(state.apply { title = lecture.title })
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
