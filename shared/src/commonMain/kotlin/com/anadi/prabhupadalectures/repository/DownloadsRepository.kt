package com.anadi.prabhupadalectures.repository

import com.anadi.prabhupadalectures.data.Database
import com.anadi.prabhupadalectures.data.lectures.Lecture
import com.anadi.prabhupadalectures.data.lectures.file
import com.anadi.prabhupadalectures.network.api.*
import com.anadi.prabhupadalectures.writeChannel
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.Serializable
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicBoolean

interface DownloadsRepository {
    val hasActiveDownloads: Boolean

    fun download(lecture: Lecture)
    fun checkPendingDownloads()
    fun observeState(): StateFlow<DownloadState>
}

class DownloadsRepositoryImpl(
    private val db: Database,
    private val api: PrabhupadaApi
) : DownloadsRepository, Serializable {

    private val downloadState = MutableStateFlow<DownloadState>(Idle)
    private val isProcessingDownload = AtomicBoolean(false)
    private val ioScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val queue = ConcurrentLinkedQueue<Lecture>()

    override val hasActiveDownloads
        get() = isProcessingDownload.get() || !queue.isEmpty()

    init {
        val list = db.selectAllDownloads()
        queue.addAll(list.filter { it.downloadProgress != FULL_PROGRESS })

        checkPendingDownloads()
    }

    override fun observeState(): StateFlow<DownloadState> = downloadState.asStateFlow()

    override fun download(lecture: Lecture) =
        lecture.run {
            when {
                remoteUrl.isBlank() -> return
                queue.any { it.id == id } -> return
                db.selectCachedLecture(id)?.isDownloaded == true -> return
            }

            val temp = copy(
                fileUrl = lecture.file.path,
                downloadProgress = ZERO_PROGRESS
            )

            queue.add(temp)
            db.insertCachedLecture(temp)
            checkPendingDownloads()
        }

    override fun checkPendingDownloads() {
        if (!isProcessingDownload.get()) {
            downloadState.value = Idle
            queue.peek()
                ?.let { startDownloadingTask(it) }
                ?: run {
                    downloadState.value = AllDownloadsCompleted
                }
        }
    }

    private fun startDownloadingTask(lecture: Lecture) =
        ioScope.launch {
            api.downloadFile(lecture.writeChannel(), lecture.remoteUrl)
                .collect { state ->
                    when (state) {
                        is Success -> handleTaskCompleted(lecture, state)
                        is Error -> {
                            Napier.e("ERRROR", state.t, "startDownloadingTask ERROR")
                            handleTaskCompleted(lecture, state)
                        }
                        is Progress -> db.insertCachedLecture(lecture.copy(downloadProgress = state.progress))
                        else -> {
                            /** do nothing **/
                        }
                    }

                    downloadState.value = state.apply { this.lecture = lecture }
                }
        }

    private fun handleTaskCompleted(lecture: Lecture, state: DownloadState) {
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

        checkPendingDownloads()
    }
}