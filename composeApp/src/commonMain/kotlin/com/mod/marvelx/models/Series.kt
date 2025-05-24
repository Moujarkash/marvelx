package com.mod.marvelx.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Series(
    @SerialName("id") val id: Int,
    @SerialName("title") val title: String,
    @SerialName("description") val description: String? = null,
    @SerialName("resourceURI") val resourceURI: String,
    @SerialName("urls") val urls: List<Url>,
    @SerialName("startYear") val startYear: Int,
    @SerialName("endYear") val endYear: Int,
    @SerialName("rating") val rating: String,
    @SerialName("type") val type: String,
    @SerialName("modified") val modified: String,
    @SerialName("thumbnail") val thumbnail: Image,
    @SerialName("creators") val creators: ResourceList,
    @SerialName("characters") val characters: ResourceList,
    @SerialName("stories") val stories: ResourceList,
    @SerialName("comics") val comics: ResourceList,
    @SerialName("events") val events: ResourceList,
    @SerialName("next") val next: ResourceSummary? = null,
    @SerialName("previous") val previous: ResourceSummary? = null
)
