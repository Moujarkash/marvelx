package com.mod.marvelx.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResourceSummary(
    @SerialName("resourceURI") val resourceURI: String,
    @SerialName("name") val name: String,
    @SerialName("role") val role: String? = null,
    @SerialName("type") val type: String? = null
)
