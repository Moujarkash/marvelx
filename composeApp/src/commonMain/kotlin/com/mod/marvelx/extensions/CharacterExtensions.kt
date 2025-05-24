package com.mod.marvelx.extensions

import com.mod.marvelx.database.entities.CharacterEntity
import com.mod.marvelx.models.Character
import com.mod.marvelx.models.Image
import com.mod.marvelx.models.ResourceList
import kotlinx.serialization.json.Json

fun Character.toEntity() = CharacterEntity(
    id = id,
    name = name,
    description = description,
    thumbnailPath = thumbnail.path,
    thumbnailExtension = thumbnail.extension,
    modified = modified,
    comicsAvailable = comics.available,
    seriesAvailable = series.available,
    storiesAvailable = stories.available,
    eventsAvailable = events.available,
    urls = Json.encodeToString(urls)
)

fun CharacterEntity.toDomain() = Character(
    id = id,
    name = name,
    description = description,
    modified = modified,
    thumbnail = Image(thumbnailPath, thumbnailExtension),
    resourceURI = "http://gateway.marvel.com/v1/public/characters/$id",
    comics = ResourceList(comicsAvailable, "", emptyList(), 0),
    series = ResourceList(seriesAvailable, "", emptyList(), 0),
    stories = ResourceList(storiesAvailable, "", emptyList(), 0),
    events = ResourceList(eventsAvailable, "", emptyList(), 0),
    urls = Json.decodeFromString(urls)
)