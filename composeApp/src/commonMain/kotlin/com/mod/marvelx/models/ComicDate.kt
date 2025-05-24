package com.mod.marvelx.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ComicDate(
    @SerialName("type") val type: String,
    @SerialName("date") val date: String
)
