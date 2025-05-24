package com.mod.marvelx.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MarvelDataContainer<T>(
    @SerialName("offset") val offset: Int,
    @SerialName("limit") val limit: Int,
    @SerialName("total") val total: Int,
    @SerialName("count") val count: Int,
    @SerialName("results") val results: List<T>
)