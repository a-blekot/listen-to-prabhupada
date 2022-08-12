package com.listentoprabhupada.common.settings

import com.russhwolf.settings.Settings

private const val KEY_SPEED = "KEY_SPEED"

const val MIN_SPEED = 1.0f
const val MAX_SPEED = 2.0f

fun Settings.saveSpeed(speed: Float) =
    putFloat(KEY_SPEED, speed.coerceIn(MIN_SPEED, MAX_SPEED))

fun Settings.getSpeed(): Float =
    getFloat(KEY_SPEED, defaultValue = MIN_SPEED)
