package com.anadi.prabhupadalectures.android.download

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.anadi.prabhupadalectures.android.R
import com.anadi.prabhupadalectures.android.download.DownloadServiceAction.ON_ACTIVITY_START
import com.anadi.prabhupadalectures.android.download.DownloadServiceAction.ON_ACTIVITY_STOP
import com.anadi.prabhupadalectures.android.util.cancelSafely
import com.anadi.prabhupadalectures.android.util.createNotificationChannel
import com.anadi.prabhupadalectures.android.util.notificationColor
import com.anadi.prabhupadalectures.android.util.notificationManager
import com.anadi.prabhupadalectures.network.api.*
import com.anadi.prabhupadalectures.repository.DOWNLOAD_NOTIFICATION_ID
import com.anadi.prabhupadalectures.repository.DownloadsRepository
import com.anadi.prabhupadalectures.repository.ToolsRepository
import dagger.hilt.android.AndroidEntryPoint
import io.github.aakira.napier.Napier
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

private const val CHANNEL_ID = "DownloadService_CHANNEL_16108"

enum class DownloadServiceAction {
    ON_ACTIVITY_START,
    ON_ACTIVITY_STOP
}

@AndroidEntryPoint
class DownloadService : Service() {

    @Inject
    lateinit var tools: ToolsRepository

    @Inject
    lateinit var downloadsRepository: DownloadsRepository

    private var isForeground = false

    private val bgScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val progressNotificationBuilder by lazy {
        NotificationCompat.Builder(this, CHANNEL_ID)
            .setProgress(FULL_PROGRESS, ZERO_PROGRESS, false)
            .setSmallIcon(R.drawable.ic_notification_download_progress)
            .setContentTitle(getString(R.string.loading))
            .setColor(notificationColor)
            .setOnlyAlertOnce(true)
            .setAutoCancel(false)
            .setColorized(true)
    }

    private val successNotificationBuilder by lazy {
        NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification_download_success)
            .setAutoCancel(true)
            .setColor(notificationColor)
            .setOnlyAlertOnce(true)
            .setColorized(true)
    }

    private val errorNotificationBuilder by lazy {
        NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification_download_error)
            .setAutoCancel(true)
            .setColor(notificationColor)
            .setOnlyAlertOnce(true)
            .setColorized(true)
    }

    override fun onCreate() {
        super.onCreate()

        createNotificationChannel(CHANNEL_ID, R.string.download_channel_name)
        observeDownloadState()
    }

    override fun onDestroy() {
        bgScope.cancelSafely("Service.onDestroy")
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.run {
            when (action) {
                ON_ACTIVITY_START.name -> onActivityStart()
                ON_ACTIVITY_STOP.name -> onActivityStop()
                else -> {
                    /** do nothing **/
                }
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    private fun onActivityStart() {
        stopForeground(true)
        isForeground = false
    }

    private fun onActivityStop() =
        if (downloadsRepository.hasActiveDownloads) {
            isForeground = true
            startForeground(DOWNLOAD_NOTIFICATION_ID, progressNotificationBuilder.build())
        } else {
            stopForeground(true)
            stopSelf()
            isForeground = false
        }

    private fun observeDownloadState() =
        bgScope.launch {
            downloadsRepository.observeDownload()
                .onEach {
                    it.print("DownloadService")
                    when (it) {
                        is Error -> notifyError(it.lecture?.title ?: "")
                        is Success -> notifySuccess(it.lecture?.title ?: "")
                        is Progress -> notifyProgress(it.lecture?.title ?: "", it.progress)
                        else -> {
                            /** do nothing **/
                        }
                    }
                }
                .collect()
        }

    private fun notifySuccess(title: String) =
        successNotificationBuilder
            .apply { setContentTitle(title) }
            .build()
            .let {
                Napier.d("notifySuccess", tag = "DownloadService")
                notificationManager?.notify(tools.getNextNotificationId(), it)
                handleDownloadCompleted()
            }

    private fun notifyError(title: String) =
        errorNotificationBuilder
            .apply { setContentTitle(title) }
            .build()
            .let {
                Napier.d("notifyError", tag = "DownloadService")
                notificationManager?.notify(tools.getNextNotificationId(), it)
                handleDownloadCompleted()
            }

    private fun notifyProgress(title: String, currentProgress: Int, maxProgress: Int = FULL_PROGRESS) =
        progressNotificationBuilder
            .apply {
                setAutoCancel(currentProgress == maxProgress)
                setProgress(maxProgress, currentProgress, false)
                setContentTitle(title)
            }
            .build()
            .let { notificationManager?.notify(DOWNLOAD_NOTIFICATION_ID, it) }

    private fun cancelProgressNotification() =
        progressNotificationBuilder
            .apply {
                setAutoCancel(true)
            }
            .build()
            .let {
                Napier.d("cancelProgressNotification", tag = "DownloadService")
                notificationManager?.run {
                    notify(DOWNLOAD_NOTIFICATION_ID, it)
                    cancel(DOWNLOAD_NOTIFICATION_ID)
                }
            }

    private fun handleDownloadCompleted() {
        cancelProgressNotification()
        stopForegroundIfNoTasksRunning()
    }

    private fun stopForegroundIfNoTasksRunning(delayMs: Long = 5000L) {
        bgScope.launch {
            Napier.d("stopForegroundIfNoTasksRunning", tag = "DownloadService")
            delay(delayMs)
            if (!downloadsRepository.hasActiveDownloads && isForeground) {
                Napier.d("stopForegroundIfNoTasksRunning invoke() !!!", tag = "DownloadService")
                stopForeground(true)
                stopSelf()
                isForeground = false
            }
        }
    }
}
