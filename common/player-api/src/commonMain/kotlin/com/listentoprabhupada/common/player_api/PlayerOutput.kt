package com.listentoprabhupada.common.player_api

sealed interface PlayerOutput {
    data class Message(val text: String) : PlayerOutput
}