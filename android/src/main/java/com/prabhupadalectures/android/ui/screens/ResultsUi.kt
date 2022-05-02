package com.prabhupadalectures.android.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.childAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.fade
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.plus
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.prabhupadalectures.android.ui.screens.results.ResultsView
import com.prabhupadalectures.lectures.mvi.lectures.Results
import com.prabhupadalectures.lectures.mvi.root.Root

@Composable
fun ResultsContent(component: Results) {
    val models = component.models.subscribeAsState()

    Box(
        Modifier
            .background(color = MaterialTheme.colors.surface)
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 40.dp),
        ) {
            items(models.value.lectures, key = { it.id }) { lectureItem ->
                Text(lectureItem.title)
            }
        }

        if (models.value.isLoading) {
            LoadingBar()
        }
    }
}


@Composable
fun LoadingBar(modifier: Modifier = Modifier) =
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .background(
                color = Color(0x44000000),
                shape = RoundedCornerShape(4.dp)
            )
    ) {
        CircularProgressIndicator()
    }

@Composable
fun MainContent(component: Root) {
    Children(routerState = component.routerState, animation = childAnimation(fade() + com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.scale())) {
        when (val child = it.instance) {
            is Root.Child.ResultsChild -> ResultsView(child.component)
        }
    }
}