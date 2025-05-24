package com.mod.marvelx.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Creator(
    @SerialName("id") val id: Int,
    @SerialName("firstName") val firstName: String,
    @SerialName("middleName") val middleName: String,
    @SerialName("lastName") val lastName: String,
    @SerialName("suffix") val suffix: String,
    @SerialName("fullName") val fullName: String,
    @SerialName("modified") val modified: String,
    @SerialName("thumbnail") val thumbnail: Image,
    @SerialName("resourceURI") val resourceURI: String,
    @SerialName("comics") val comics: ResourceList,
    @SerialName("series") val series: ResourceList,
    @SerialName("stories") val stories: ResourceList,
    @SerialName("events") val events: ResourceList,
    @SerialName("urls") val urls: List<Url>
)
