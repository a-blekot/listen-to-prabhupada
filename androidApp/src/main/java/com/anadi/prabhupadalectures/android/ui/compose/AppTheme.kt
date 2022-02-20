package com.anadi.prabhupadalectures.android.ui.compose

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val BrownDark = Color(0xFF603004)
private val BrownMed = Color(0xB2DE924D)
private val BrownMedLight = Color(0xFFFEC795)
private val BrownLight = Color(0xB2FFECDB)
private val BrownXLight = Color(0xFFFFFFFF)
//private val BrownXLight = Color(0xFFE5E5E5)

private val Orange = Color(0xFFB65D0D)
val GrayLight = Color(0xFF919191)

private val LightColors = lightColors(
    primary = BrownMed,

    primaryVariant = BrownMedLight,
    onPrimary = BrownDark,

    secondary = BrownLight,
    onSecondary = BrownDark,

    surface = BrownXLight,
    onSurface = Orange,

    background = BrownXLight,
    onBackground = Orange
)

private val DarkColors = darkColors(
    primary = BrownLight,
    primaryVariant = BrownLight,
    onPrimary = BrownMed,
    secondary = BrownMed,
    onSecondary = BrownMed,
    surface = BrownDark,
    background = BrownDark
)

@Composable
fun AppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = if (isSystemInDarkTheme()) DarkColors else LightColors,
        content = {
            Surface(content = content)
        }
    )
}