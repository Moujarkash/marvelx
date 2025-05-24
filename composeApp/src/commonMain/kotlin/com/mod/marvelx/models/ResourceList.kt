package com.mod.marvelx.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResourceList(
    @SerialName("available") val available: Int,
    @SerialName("collectionURI") val collectionURI: String,
    @SerialName("items") val items: List<ResourceSummary>,
    @SerialName("returned") val returned: Int
)
