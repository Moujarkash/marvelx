package com.mod.marvelx.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Comic(
    @SerialName("id") val id: Int,
    @SerialName("digitalId") val digitalId: Int,
    @SerialName("title") val title: String,
    @SerialName("issueNumber") val issueNumber: Double,
    @SerialName("variantDescription") val variantDescription: String,
    @SerialName("description") val description: String? = null,
    @SerialName("modified") val modified: String,
    @SerialName("isbn") val isbn: String,
    @SerialName("upc") val upc: String,
    @SerialName("diamondCode") val diamondCode: String,
    @SerialName("ean") val ean: String,
    @SerialName("issn") val issn: String,
    @SerialName("format") val format: String,
    @SerialName("pageCount") val pageCount: Int,
    @SerialName("textObjects") val textObjects: List<TextObject>,
    @SerialName("resourceURI") val resourceURI: String,
    @SerialName("urls") val urls: List<Url>,
    @SerialName("series") val series: ResourceSummary,
    @SerialName("variants") val variants: List<ResourceSummary>,
    @SerialName("collections") val collections: List<ResourceSummary>,
    @SerialName("collectedIssues") val collectedIssues: List<ResourceSummary>,
    @SerialName("dates") val dates: List<ComicDate>,
    @SerialName("prices") val prices: List<ComicPrice>,
    @SerialName("thumbnail") val thumbnail: Image,
    @SerialName("images") val images: List<Image>,
    @SerialName("creators") val creators: ResourceList,
    @SerialName("characters") val characters: ResourceList,
    @SerialName("stories") val stories: ResourceList,
    @SerialName("events") val events: ResourceList
)
