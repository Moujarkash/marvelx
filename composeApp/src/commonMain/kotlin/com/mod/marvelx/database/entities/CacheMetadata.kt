package com.mod.marvelx.database.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "cache_metadata",
    indices = [Index(value = ["requestKey"], unique = true)]
)
data class CacheMetadata(
    @PrimaryKey val id: String,
    val requestKey: String,
    val etag: String,
    val lastFetched: Long,
    val offset: Int,
    val limit: Int,
    val total: Int,
    val entityType: String,
    val queryParams: String? = null,
    val expirationTime: Long = Long.MAX_VALUE
)