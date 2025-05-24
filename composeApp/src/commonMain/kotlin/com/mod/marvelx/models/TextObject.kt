package com.mod.marvelx.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TextObject(
    @SerialName("type") val type: String,
    @SerialName("language") val language: String,
    @SerialName("text") val text: String
)
