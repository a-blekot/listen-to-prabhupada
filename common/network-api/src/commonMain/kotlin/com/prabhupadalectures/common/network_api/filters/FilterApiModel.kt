package com.listentoprabhupada.common.network_api.filters

import kotlinx.serialization.Serializable

@Serializable
data class FilterApiModel(
    val name: String, //  categories, t,         quotes__scripture, date__year, country, locality, quotes__chapter, quotes__verse
    val title: String, // Category,   File type, Scripture,         Год,        Country, Locality, Chapter,       , Verse
    val parent: String?, // other filter's name (e.g. quotes__scripture)
    val options: List<OptionApiModel>
)