package com.listentoprabhupada.android.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.listentoprabhupada.android_ui.helpers.AlphaDarkBg

@Composable
fun LoadingBar(modifier: Modifier = Modifier, bgColor: Color = AlphaDarkBg) =
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .background(
                color = bgColor,
                shape = RoundedCornerShape(4.dp)
            )
    ) {
        CircularProgressIndicator()
    }

@Preview
@Composable
fun PreviewLoadingBar() =
    LoadingBar()