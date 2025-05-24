package com.mod.marvelx.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Entity(tableName = "characters")
data class CharacterEntity @OptIn(ExperimentalTime::class) constructor(
    @PrimaryKey val id: Int,
    val name: String,
    val description: String,
    val thumbnailPath: String,
    val thumbnailExtension: String,
    val modified: String,
    val comicsAvailable: Int,
    val seriesAvailable: Int,
    val storiesAvailable: Int,
    val eventsAvailable: Int,
    val urls: String,
    val lastUpdated: Long = Clock.System.now().toEpochMilliseconds()
)
