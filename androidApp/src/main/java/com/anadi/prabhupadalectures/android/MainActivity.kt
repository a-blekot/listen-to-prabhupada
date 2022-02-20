package com.anadi.prabhupadalectures.android

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import cafe.adriel.voyager.navigator.Navigator
import com.anadi.prabhupadalectures.android.PrabhupadaApp.Companion.app
import com.anadi.prabhupadalectures.android.ui.compose.*
import com.anadi.prabhupadalectures.data.lectures.Lecture
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues

const val EXTRA_LECTURE_URL = "EXTRA_LECTURE_URL"
const val EXTRA_LECTURE_DURATION = "EXTRA_LECTURE_DURATION"
const val EXTRA_LECTURE_TITLE = "EXTRA_LECTURE_TITLE"
const val EXTRA_LECTURE_DESCRIPTION = "EXTRA_LECTURE_DESCRIPTION"

class MainActivity : ComponentActivity(), ServiceConnection {

    private val uiListener: (UIAction) -> Unit = { uiAction ->
        when (uiAction) {
            is PlayClick -> playLecture(uiAction.lecture)
            is OptionClick -> {
                lifecycleScope.launchWhenStarted {
                    app.dataModel.updateQuery(uiAction.queryParam)
                }
            }
            is FavoriteClick -> {
                if (uiAction.isFavorite) {
                    app.dataModel.addFavorite(uiAction.lectureId)
                } else {
                    app.dataModel.removeFavorite(uiAction.lectureId)
                }
            }
        }
    }

    private var boundService: PlaybackService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DebugLog.d("PlaybackService", "ACTIVITY::onCreate")

        setContent {
            AppTheme {
                ProvideWindowInsets {
                    Box(
                        Modifier.padding(
                            rememberInsetsPaddingValues(
                                insets = LocalWindowInsets.current.systemBars,
                                applyStart = true,
                                applyTop = false,
                                applyEnd = true,
                                applyBottom = false
                            )
                        )
                    ) {
                        Navigator(MainScreen(uiListener))
                    }
                }
            }
        }


    }

    private fun playLecture(lecture: Lecture) {
        DebugLog.d("PlaybackService", "ACTIVITY::playLecture")
        val intent = Intent(this, PlaybackService::class.java)
        intent.putExtra(EXTRA_LECTURE_URL, lecture.fileInfo.mediaStreamUrl)
        intent.putExtra(EXTRA_LECTURE_DURATION, lecture.fileInfo.durationMillis)
        intent.putExtra(EXTRA_LECTURE_TITLE, lecture.displayedTitle)
        intent.putExtra(EXTRA_LECTURE_DESCRIPTION, lecture.displayedDescription)
        startService(intent)
        bindService(intent, this, Context.BIND_IMPORTANT)
    }

    override fun onStart() {
        super.onStart()
        boundService?.stopForeground(true)
        DebugLog.d("PlaybackService", "ACTIVITY::onStart")
        DebugLog.d("PlaybackService", "ACTIVITY boundService = $boundService")
    }

    override fun onStop() {
        super.onStop()
        boundService?.goForeground()
        DebugLog.d("PlaybackService", "ACTIVITY::onStop")
        DebugLog.d("PlaybackService", "ACTIVITY boundService = $boundService")
    }

    override fun onDestroy() {
        super.onDestroy()
        DebugLog.d("PlaybackService", "ACTIVITY::onDestroy")
    }

    override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
        boundService = (binder as? PlaybackService.PlaybackBinder)?.service
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        boundService = null
    }
}
