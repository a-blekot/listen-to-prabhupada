package com.listentoprabhupada.common.utils

import com.listentoprabhupada.common.data.Locales.be
import com.listentoprabhupada.common.data.Locales.en
import com.listentoprabhupada.common.data.Locales.ka
import com.listentoprabhupada.common.data.Locales.kk
import com.listentoprabhupada.common.data.Locales.ru
import com.listentoprabhupada.common.data.Locales.uk
import com.listentoprabhupada.common.settings.getLocale
import com.listentoprabhupada.common.settings.saveLocale
//import dev.icerock.moko.resources.desc.StringDesc TODO moko

fun checkLocale(systemLocale: String) {
    if (getLocale().isNotBlank()) {
        setAppLocale(getLocale())
        return // means that locale already selected by user
    }

    setAppLocale(systemLocale)
}

fun setAppLocale(locale: String) =
    when(locale) {
        uk -> setUkrainian()
        ru, be, ka, kk -> setRussian()
        else -> setEnglish()
    }

private fun setUkrainian() {
    saveLocale(uk)
//    StringDesc.localeType = StringDesc.LocaleType.Custom(uk)
}

private fun setRussian() {
    saveLocale(ru)
//    StringDesc.localeType = StringDesc.LocaleType.Custom(ru)
}

private fun setEnglish() {
    saveLocale(en)
//    StringDesc.localeType = StringDesc.LocaleType.Custom(en)
}