package com.listentoprabhupada.common.utils

expect object Strings {
    fun get(id: String): String
    fun get(id: String, quantity: Int): String
    fun format(id: String, vararg formatArgs: Any): String
}