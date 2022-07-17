package com.listentoprabhupada.common.utils

fun interface Consumer<T> {
    fun onNext(t: T)

    operator fun invoke(t: T) = onNext(t)
}

