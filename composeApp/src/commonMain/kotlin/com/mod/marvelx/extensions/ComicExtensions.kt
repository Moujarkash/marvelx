package com.mod.marvelx.extensions

import com.mod.marvelx.database.entities.ComicEntity
import com.mod.marvelx.models.Comic
import com.mod.marvelx.models.Image
import com.mod.marvelx.models.ResourceList
import com.mod.marvelx.models.ResourceSummary
import kotlinx.serialization.json.Json

fun Comic.toEntity() = ComicEntity(
    id = id,
    digitalId = digitalId,
    title = title,
    issueNumber = issueNumber,
    description = description,
    thumbnailPath = thumbnail.path,
    thumbnailExtension = thumbnail.extension,
    pageCount = pageCount,
    modified = modified,
    seriesName = series.name,
    creatorsAvailable = creators.available,
    charactersAvailable = characters.available,
    dates = Json.encodeToString(dates),
    prices = Json.encodeToString(prices)
)

fun ComicEntity.toDomain() = Comic(
    id = id,
    digitalId = digitalId,
    title = title,
    issueNumber = issueNumber,
    variantDescription = "",
    description = description,
    modified = modified,
    isbn = "",
    upc = "",
    diamondCode = "",
    ean = "",
    issn = "",
    format = "",
    pageCount = pageCount,
    textObjects = emptyList(),
    resourceURI = "http://gateway.marvel.com/v1/public/comics/$id",
    urls = emptyList(),
    series = ResourceSummary("", seriesName),
    variants = emptyList(),
    collections = emptyList(),
    collectedIssues = emptyList(),
    dates = Json.decodeFromString(dates),
    prices = Json.decodeFromString(prices),
    thumbnail = Image(thumbnailPath, thumbnailExtension),
    images = emptyList(),
    creators = ResourceList(creatorsAvailable, "", emptyList(), 0),
    characters = ResourceList(charactersAvailable, "", emptyList(), 0),
    stories = ResourceList(0, "", emptyList(), 0),
    events = ResourceList(0, "", emptyList(), 0)
)