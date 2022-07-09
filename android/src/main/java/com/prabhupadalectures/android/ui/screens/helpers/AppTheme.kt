package com.prabhupadalectures.android.ui.screens.helpers

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val BrownDark = Color(0xFF603004)
private val BrownMed = Color(0xB2DE924D)
private val BrownMedLight = Color(0xFFFEC795)
val BrownMedLight2 = Color(0xFFE8B07E)
private val BrownLight = Color(0xB2FFECDB)
private val BrownXLight = Color(0xFFFFFFFF)
val AlphaDarkBg = Color(0x44000000)
//private val BrownXLight = Color(0xFFE5E5E5)

val favoriteSelected = Color(0xFFD0690D)
val favoriteUnselected = Color(0xFFFEEAD8)
val playerBg = Color(0xFFFFF7EF)

private val Orange = Color(0xFFB65D0D)
//private val OrangeLight = Color(0xFFFAD1B4) ?? BrownMedLight
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