package com.mod.marvelx.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Image(
    @SerialName("path") val path: String,
    @SerialName("extension") val extension: String
) {
    fun getImageUrl(variant: String = "standard_medium"): String {
        return "$path/$variant.$extension"
    }
}
