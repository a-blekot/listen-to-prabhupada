package com.anadi.prabhupadalectures.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import cafe.adriel.voyager.navigator.Navigator
import com.anadi.prabhupadalectures.Greeting
import com.anadi.prabhupadalectures.android.ui.compose.AppTheme
import com.anadi.prabhupadalectures.android.ui.compose.MainScreen
import com.anadi.prabhupadalectures.datamodel.DataModel
import com.anadi.prabhupadalectures.datamodel.QueryParam
import com.anadi.prabhupadalectures.network.api.createPrabhupadaApi
import com.anadi.prabhupadalectures.repository.RepositoryImpl
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import kotlinx.coroutines.delay

fun greet(): String {
    return Greeting().greeting()
}

class MainActivity : ComponentActivity() {

//    private val userApi: UserApi = createUserApi()
//    private val userRepository: UsersRepository = UserRepositoryImpl(userApi)
//    private val userDataModel = UserDataModel(userRepository, BuildConfig.DEBUG)

    private val api = createPrabhupadaApi()
    private val repository = RepositoryImpl(api)
    private val dataModel = DataModel(repository, BuildConfig.DEBUG)

    private val exoCallback: (String) -> Unit = {
        playLecture(it)
    }

    private val onOptionSelected: (QueryParam) -> Unit = { queryParam ->
        lifecycleScope.launchWhenStarted {
            dataModel.updateQuery(queryParam)
        }
    }

    private var exoPlayer: ExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        exoPlayer = ExoPlayer.Builder(this).build().apply {
            volume = 1.0f
        }

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
                        Navigator(MainScreen(dataModel, onOptionSelected, exoCallback))
                    }
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            delay(1000L)
            dataModel.loadMore()
        }
    }

    override fun onResume() {
        super.onResume()
        resumeLecture()
    }

    override fun onPause() {
        super.onPause()
        pauseLecture()
    }

    override fun onDestroy() {
        super.onDestroy()
        exoPlayer?.release()
    }

    private fun playLecture(url: String) {
        exoPlayer?.apply{
            setMediaItem(MediaItem.fromUri(url))
            prepare()
            playWhenReady = true
        }
    }

    private fun pauseLecture() {
        exoPlayer?.playWhenReady = false
    }

    private fun resumeLecture() {
        exoPlayer?.playWhenReady = true
    }
}
