package com.mod.marvelx.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Entity(tableName = "comics")
data class ComicEntity @OptIn(ExperimentalTime::class) constructor(
    @PrimaryKey val id: Int,
    val digitalId: Int,
    val title: String,
    val issueNumber: Double,
    val description: String?,
    val thumbnailPath: String,
    val thumbnailExtension: String,
    val pageCount: Int,
    val modified: String,
    val seriesName: String,
    val creatorsAvailable: Int,
    val charactersAvailable: Int,
    val dates: String,
    val prices: String,
    val lastUpdated: Long = Clock.System.now().toEpochMilliseconds()
)