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
import com.anadi.prabhupadalectures.android.ui.compose.AppTheme
import com.anadi.prabhupadalectures.android.ui.compose.MainScreen
import com.anadi.prabhupadalectures.data.Database
import com.anadi.prabhupadalectures.data.DatabaseDriverFactory
import com.anadi.prabhupadalectures.data.lectures.Lecture
import com.anadi.prabhupadalectures.datamodel.DataModel
import com.anadi.prabhupadalectures.datamodel.QueryParam
import com.anadi.prabhupadalectures.network.api.createPrabhupadaApi
import com.anadi.prabhupadalectures.repository.RepositoryImpl
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import kotlinx.coroutines.delay

const val EXTRA_LECTURE_URL = "EXTRA_LECTURE_URL"
const val EXTRA_LECTURE_DURATION = "EXTRA_LECTURE_DURATION"
const val EXTRA_LECTURE_TITLE = "EXTRA_LECTURE_TITLE"
const val EXTRA_LECTURE_DESCRIPTION = "EXTRA_LECTURE_DESCRIPTION"

class MainActivity : ComponentActivity(), ServiceConnection {

//    private val userApi: UserApi = createUserApi()
//    private val userRepository: UsersRepository = UserRepositoryImpl(userApi)
//    private val userDataModel = UserDataModel(userRepository, BuildConfig.DEBUG)

    private val api = createPrabhupadaApi()
    private val repository = RepositoryImpl(api)
    private val db = Database(DatabaseDriverFactory(this))
    private val dataModel = DataModel(db, repository, BuildConfig.DEBUG)

    private val exoCallback: (Lecture) -> Unit = {
        playLecture(it)
    }

    private val onFavorite: (Long, Boolean) -> Unit = { id, isFavorite ->
        if (isFavorite) {
            dataModel.addFavorite(id)
        } else {
            dataModel.removeFavorite(id)
        }
    }

    private val onOptionSelected: (QueryParam) -> Unit = { queryParam ->
        lifecycleScope.launchWhenStarted {
            dataModel.updateQuery(queryParam)
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
                        Navigator(MainScreen(dataModel, onOptionSelected, exoCallback, onFavorite))
                    }
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            delay(200L)
            dataModel.init()
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
