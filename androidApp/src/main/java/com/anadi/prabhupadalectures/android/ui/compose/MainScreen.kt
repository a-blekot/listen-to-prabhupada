package com.anadi.prabhupadalectures.android.ui.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import com.anadi.prabhupadalectures.android.util.connectivityState
import com.anadi.prabhupadalectures.repository.Repository
import com.anadi.prabhupadalectures.utils.ConnectionState

class MainScreen(
    private val repository: Repository,
    private val uiListener: ((UIAction) -> Unit)? = null
) : Screen {

    @Composable
    override fun Content() {
        val connection by connectivityState()

        when (connection) {
            ConnectionState.Online -> ContentComposable(repository, uiListener)
            else -> OfflineComposable()
        }
    }
}
