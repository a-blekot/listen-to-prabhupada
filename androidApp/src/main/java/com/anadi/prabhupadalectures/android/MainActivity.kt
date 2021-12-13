package com.anadi.prabhupadalectures.android

import android.os.Bundle
import com.anadi.prabhupadalectures.Greeting
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import cafe.adriel.voyager.navigator.Navigator
import com.anadi.prabhupadalectures.android.ui.compose.AppTheme
import com.anadi.prabhupadalectures.android.ui.compose.MainScreen
import com.anadi.prabhupadalectures.datamodel.UserDataModel
import com.anadi.prabhupadalectures.network.api.UserApi
import com.anadi.prabhupadalectures.network.api.UserApiImpl
import com.anadi.prabhupadalectures.network.api.createUserApi
import com.anadi.prabhupadalectures.repository.UserRepositoryImpl
import com.anadi.prabhupadalectures.repository.UsersRepository
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import kotlinx.coroutines.delay

fun greet(): String {
    return Greeting().greeting()
}

class MainActivity : ComponentActivity() {

    private val userApi: UserApi = createUserApi()
    private val userRepository: UsersRepository = UserRepositoryImpl(userApi)
    private val userDataModel = UserDataModel(userRepository, BuildConfig.DEBUG)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
                        Navigator(MainScreen(userDataModel))
                    }
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            delay(1000L)
            userDataModel.loadMore()
        }
    }
}
