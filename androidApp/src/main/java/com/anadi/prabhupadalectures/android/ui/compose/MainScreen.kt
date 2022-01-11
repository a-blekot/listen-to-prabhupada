package com.anadi.prabhupadalectures.android.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.anadi.prabhupadalectures.datamodel.DataModel
import com.anadi.prabhupadalectures.datamodel.QueryParam

class MainScreen(
    private val dataModel: DataModel,
    private val onOptionSelected: ((QueryParam) -> Unit)? = null,
    private val exoCallback: (String) -> Unit
) : Screen {

    @Composable
    override fun Content() {
        val state = dataModel.observeState().collectAsState()

        Box(
            Modifier
                .background(color = MaterialTheme.colors.surface)
                .padding(all = 12.dp)) {
            val coroutineScope = rememberCoroutineScope()

            LazyColumn(
                Modifier.fillMaxWidth()
            ) {
                val isNotEmpty = state.value.lectures.isNotEmpty()

                if (isNotEmpty) {
                    item { Header() }
                    item { FilterButton() }
                }

                items(state.value.lectures) { LectureListItem(it, exoCallback) }

                if (isNotEmpty) {
                    item { PageControl(1, 309) }
                }

                items(state.value.filters) { FilterListItem(it, onOptionSelected) }

            }
        }
    }
}
