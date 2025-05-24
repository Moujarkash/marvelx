package com.mod.marvelx.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Story(
    @SerialName("id") val id: Int,
    @SerialName("title") val title: String,
    @SerialName("description") val description: String,
    @SerialName("resourceURI") val resourceURI: String,
    @SerialName("type") val type: String,
    @SerialName("modified") val modified: String,
    @SerialName("thumbnail") val thumbnail: Image? = null,
    @SerialName("creators") val creators: ResourceList,
    @SerialName("characters") val characters: ResourceList,
    @SerialName("series") val series: ResourceList,
    @SerialName("comics") val comics: ResourceList,
    @SerialName("events") val events: ResourceList,
    @SerialName("originalIssue") val originalIssue: ResourceSummary? = null
)
