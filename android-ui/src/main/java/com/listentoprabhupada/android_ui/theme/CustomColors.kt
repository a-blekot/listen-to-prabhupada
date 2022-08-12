package com.listentoprabhupada.android_ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private object LightColors {
    private val primary2 = Color(0xFFCB8552)
    private val primary3 = Color(0xFFE8B07E)

    private val secondary = Color(0xFFFFD5AF)
    private val secondary2 = Color(0xFFFEEAD8)
    private val secondary3 = Color(0xFFFFE7D1)
    private val secondary4 = Color(0xFFDE924D)
    private val secondary5 = Color(0xFFFAD1B4)
    private val secondary6 = Color(0xFFFFEADA)

    private val gray1 = Color(0xFFB0B0B0)
    private val gray2 = Color(0xFF9E9E9E)

    val primary = Color(0xFFD0690D)
    val tertiary = Color(0xFF603004)
    val background = Color(0xFFFFFFFF)
    val surface = Color(0xFFFFF7EF)

    val toolbar = surface
    val chipsBg = surface
    val chipsContent = primary

    val lectureTitle = primary
    val lectureDescr = gray1
    val lecturePlay = primary3
    val lecturePause = primary3

    val favSelected = primary
    val favUnselected = secondary2
    val btnPages = surface

    val playerBg = surface
    val playerTimeLineSelector = primary
    val playerTimeLineBg = secondary5
    val playerTimer = tertiary
    val playerTitle = tertiary
    val playerDescr = gray2
    val playerButtons = tertiary
    val playerSpeedBg = secondary6

    val navBarBg = secondary6
    val navBarText = tertiary
    val navBarIcon = tertiary
    val navBarIconSelected = secondary2
    val navBarIconSelectedBg = primary
    val navBarIconSelectedText = primary

    val filtersCategory = secondary4.copy(alpha = 0.7f)
    val filtersSelected = secondary3
    val filtersNeutral = surface
    val filtersText = tertiary
    val filtersCountText = primary2

    val chipBg = surface
    val chipText = tertiary
    val chipIconBg = primary
    val chipIcon = tertiary
}

private object DarkColors {
    private val primary2 = Color(0xFFCB8552)
    private val primary3 = Color(0xFFE8B07E)

    private val secondary = Color(0xFFFFD5AF)
    private val secondary2 = Color(0xFFFEEAD8)
    private val secondary3 = Color(0xFFFFE7D1)
    private val secondary4 = Color(0xFFDE924D)
    private val secondary5 = Color(0xFFFAD1B4)
    private val secondary6 = Color(0xFFFFEADA)

    private val gray1 = Color(0xFFB0B0B0)
    private val gray2 = Color(0xFF9E9E9E)

    val primary = Color(0xFFD0690D)
    val tertiary = Color(0xFF603004)
    val background = Color(0xFFFFFFFF)
    val surface = Color(0xFFFFF7EF)

    val toolbar = surface
    val chipsBg = surface
    val chipsContent = primary

    val lectureTitle = primary
    val lectureDescr = gray1
    val lecturePlay = primary3
    val lecturePause = primary3

    val favSelected = primary
    val favUnselected = secondary2
    val btnPages = surface

    val playerBg = surface
    val playerTimeLineSelector = primary
    val playerTimeLineBg = secondary5
    val playerTimer = tertiary
    val playerTitle = tertiary
    val playerDescr = gray2
    val playerButtons = tertiary
    val playerSpeedBg = secondary6

    val navBarBg = secondary6
    val navBarText = tertiary
    val navBarIcon = tertiary
    val navBarIconSelected = secondary2
    val navBarIconSelectedBg = primary
    val navBarIconSelectedText = primary

    val filtersCategory = secondary4.copy(alpha = 0.7f)
    val filtersSelected = secondary3
    val filtersNeutral = surface
    val filtersText = tertiary
    val filtersCountText = primary2

    val chipBg = surface
    val chipText = tertiary
    val chipIconBg = primary
    val chipIcon = tertiary
}

object Colors {
    @Composable
    fun background() = dayNight(LightColors.background, DarkColors.background)

    @Composable
    fun surface() = dayNight(LightColors.surface, DarkColors.surface)

    @Composable
    fun primary() = dayNight(LightColors.primary, DarkColors.primary)

    @Composable
    fun tertiary() = dayNight(LightColors.tertiary, DarkColors.tertiary)

    @Composable
    fun toolbar() = dayNight(LightColors.toolbar, DarkColors.toolbar)

    @Composable
    fun chipsBg() = dayNight(LightColors.chipsBg, DarkColors.chipsBg)

    @Composable
    fun chipsContent() = dayNight(LightColors.chipsContent, DarkColors.chipsContent)

    @Composable
    fun lectureTitle() = dayNight(LightColors.lectureTitle, DarkColors.lectureTitle)

    @Composable
    fun lectureDescr() = dayNight(LightColors.lectureDescr, DarkColors.lectureDescr)

    @Composable
    fun lecturePlay() = dayNight(LightColors.lecturePlay, DarkColors.lecturePlay)

    @Composable
    fun lecturePause() = dayNight(LightColors.lecturePause, DarkColors.lecturePause)

    @Composable
    fun favSelected() = dayNight(LightColors.favSelected, DarkColors.favSelected)

    @Composable
    fun favUnselected() = dayNight(LightColors.favUnselected, DarkColors.favUnselected)

    @Composable
    fun btnPages() = dayNight(LightColors.btnPages, DarkColors.btnPages)

    @Composable
    fun playerBg() = dayNight(LightColors.playerBg, DarkColors.playerBg)

    @Composable
    fun playerTimeLineBg() = dayNight(LightColors.playerTimeLineBg, DarkColors.playerTimeLineBg)

    @Composable
    fun playerTimeLineSelector() =
        dayNight(LightColors.playerTimeLineSelector, DarkColors.playerTimeLineSelector)

    @Composable
    fun playerTimer() = dayNight(LightColors.playerTimer, DarkColors.playerTimer)

    @Composable
    fun playerTitle() = dayNight(LightColors.playerTitle, DarkColors.playerTitle)

    @Composable
    fun playerDescr() = dayNight(LightColors.playerDescr, DarkColors.playerDescr)

    @Composable
    fun playerButtons() = dayNight(LightColors.playerButtons, DarkColors.playerButtons)

    @Composable
    fun playerSpeedBg() = dayNight(LightColors.playerSpeedBg, DarkColors.playerSpeedBg)

    @Composable
    fun navBarBg() = dayNight(LightColors.navBarBg, DarkColors.navBarBg)

    @Composable
    fun navBarIcon() = dayNight(LightColors.navBarIcon, DarkColors.navBarIcon)

    @Composable
    fun navBarIconSelected() =
        dayNight(LightColors.navBarIconSelected, DarkColors.navBarIconSelected)

    @Composable
    fun navBarIconSelectedBg() =
        dayNight(LightColors.navBarIconSelectedBg, DarkColors.navBarIconSelectedBg)

    @Composable
    fun navBarIconSelectedText() =
        dayNight(LightColors.navBarIconSelectedText, DarkColors.navBarIconSelectedText)

    @Composable
    fun navBarText() = dayNight(LightColors.navBarText, DarkColors.navBarText)

    @Composable
    fun filtersText() = dayNight(LightColors.filtersText, DarkColors.filtersText)

    @Composable
    fun filtersCountText() = dayNight(LightColors.filtersCountText, DarkColors.filtersCountText)

    @Composable
    fun filtersCategory() = dayNight(LightColors.filtersCategory, DarkColors.filtersCategory)

    @Composable
    fun filtersSelected() = dayNight(LightColors.filtersSelected, DarkColors.filtersSelected)

    @Composable
    fun filtersNeutral() = dayNight(LightColors.filtersNeutral, DarkColors.filtersNeutral)

    @Composable
    fun chipBg() = dayNight(LightColors.chipBg, DarkColors.chipBg)

    @Composable
    fun chipText() = dayNight(LightColors.chipText, DarkColors.chipText)

    @Composable
    fun chipIconBg() = dayNight(LightColors.chipIconBg, DarkColors.chipIconBg)

    @Composable
    fun chipIcon() = dayNight(LightColors.chipIcon, DarkColors.chipIcon)

    @Composable
    private fun dayNight(day: Color, night: Color) =
        when {
            isSystemInDarkTheme() -> day
            else -> night
        }
}



