package com.anadi.prabhupadalectures.android.ui.compose

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun Header(lecturesCount: Int) =
    Text(text = "Header lecturesCount = $lecturesCount")