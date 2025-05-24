package com.mod.marvelx.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Character(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("description") val description: String,
    @SerialName("modified") val modified: String,
    @SerialName("thumbnail") val thumbnail: Image,
    @SerialName("resourceURI") val resourceURI: String,
    @SerialName("comics") val comics: ResourceList,
    @SerialName("series") val series: ResourceList,
    @SerialName("stories") val stories: ResourceList,
    @SerialName("events") val events: ResourceList,
    @SerialName("urls") val urls: List<Url>
)
