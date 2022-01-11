package com.anadi.prabhupadalectures.android.ui.compose

import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun FilterButton(modifier: Modifier = Modifier) =
    OutlinedButton(
        modifier = modifier,
        onClick = { /* Do something! */ },
        colors = ButtonDefaults.textButtonColors(backgroundColor = Color.DarkGray)
    ) {
        Text("Filters")
    }