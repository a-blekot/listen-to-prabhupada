package com.anadi.prabhupadalectures.android.ui.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.anadi.prabhupadalectures.datamodel.UserDataModel
import kotlinx.coroutines.launch

class MainScreen(private val userDataModel: UserDataModel) : Screen {

    @Composable
    override fun Content() {
        val state = userDataModel.observeState().collectAsState()

        Box(
            modifier = Modifier.padding(all = 12.dp)
        ) {
            val coroutineScope = rememberCoroutineScope()
            Users(state.value.users) {
                coroutineScope.launch { userDataModel.loadMore() }
            }
        }
    }
}