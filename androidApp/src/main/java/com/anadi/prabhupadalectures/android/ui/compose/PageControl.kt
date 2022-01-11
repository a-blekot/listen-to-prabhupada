package com.anadi.prabhupadalectures.android.ui.compose

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun PageControl(page: Int, total: Int, modifier: Modifier = Modifier) =
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("$page из $total")
        Spacer(modifier.weight(1.0f))
        Button(
            modifier = modifier,
            onClick = { /* Do something! */ },
            colors = ButtonDefaults.textButtonColors(
                backgroundColor = MaterialTheme.colors.primaryVariant,
            )
        ) {
            Text(
                text = "Filters",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colors.onSecondary,
                style = MaterialTheme.typography.body1
            )
        }
    }

