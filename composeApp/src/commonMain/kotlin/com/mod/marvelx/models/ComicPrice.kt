package com.mod.marvelx.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ComicPrice(
    @SerialName("type") val type: String,
    @SerialName("price") val price: Double
)
