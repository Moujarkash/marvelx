package com.mod.marvelx.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Entity(
    tableName = "character_comic_cross_ref",
    primaryKeys = ["characterId", "comicId"],
    foreignKeys = [
        ForeignKey(
            entity = CharacterEntity::class,
            parentColumns = ["id"],
            childColumns = ["characterId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ComicEntity::class,
            parentColumns = ["id"],
            childColumns = ["comicId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["characterId"]), Index(value = ["comicId"])]
)
data class CharacterComicCrossRef @OptIn(ExperimentalTime::class) constructor(
    val characterId: Int,
    val comicId: Int,
    val createdAt: Long = Clock.System.now().toEpochMilliseconds()
)
