package com.mod.marvelx.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Event(
    @SerialName("id") val id: Int,
    @SerialName("title") val title: String,
    @SerialName("description") val description: String,
    @SerialName("resourceURI") val resourceURI: String,
    @SerialName("urls") val urls: List<Url>,
    @SerialName("modified") val modified: String,
    @SerialName("start") val start: String? = null,
    @SerialName("end") val end: String? = null,
    @SerialName("thumbnail") val thumbnail: Image,
    @SerialName("creators") val creators: ResourceList,
    @SerialName("characters") val characters: ResourceList,
    @SerialName("stories") val stories: ResourceList,
    @SerialName("comics") val comics: ResourceList,
    @SerialName("series") val series: ResourceList,
    @SerialName("next") val next: ResourceSummary? = null,
    @SerialName("previous") val previous: ResourceSummary? = null
)
