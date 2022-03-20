package com.anadi.prabhupadalectures.android.ui.compose

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.anadi.prabhupadalectures.android.util.connectivityState
import com.anadi.prabhupadalectures.utils.ConnectionState

class MainScreen() : Screen {

    @Composable
    override fun Content() {
        val connection by connectivityState()

        when (connection) {
            ConnectionState.Online -> ContentComposable()
            else -> OfflineComposable()
        }
    }
}
class TestScreen() : Screen {
    @Composable
    override fun Content() {
        Text(
            text = "SUPER PLUSH",
            color = MaterialTheme.colors.onPrimary,
            style = MaterialTheme.typography.h3,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = 60.dp)
        )
    }
}
