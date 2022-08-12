package com.listentoprabhupada.android_ui.helpers

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import com.listentoprabhupada.android_ui.theme.Colors
import com.listentoprabhupada.android_ui.theme.Colors.playerTimer


private val list = listOf(
    Speedy("1x", 1f),
    Speedy("1,25x", 1.25f),
    Speedy("1,5x", 1.5f),
    Speedy("1,75x", 1.75f),
    Speedy("2x", 2f),
)

private class Speedy(val title: String, val speed: Float)

@Composable
fun SpeedDropdown(expanded: MutableState<Boolean>, onChange: (Float) -> Unit) {

    DropdownMenu(
        expanded = expanded.value,
        onDismissRequest = { expanded.value = false }
    ) {
        list.forEach {
            DropdownMenuItem(
                text = {
                    Text(
                        text = it.title,
                        maxLines = 1,
                        overflow = Ellipsis,
                        color = playerTimer(),
                        style = typography.labelLarge,
                        textAlign = TextAlign.Center
                    )
                },
                onClick = {
                    expanded.value = false
                    onChange(it.speed)
                }
            )
        }
    }
}

