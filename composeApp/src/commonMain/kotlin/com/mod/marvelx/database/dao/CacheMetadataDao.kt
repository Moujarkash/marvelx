package com.mod.marvelx.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mod.marvelx.database.entities.CacheMetadata

@Dao
interface CacheMetadataDao {
    @Query("SELECT * FROM cache_metadata WHERE requestKey = :requestKey")
    suspend fun getCacheMetadata(requestKey: String): CacheMetadata?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCacheMetadata(metadata: CacheMetadata)

    @Query("DELETE FROM cache_metadata WHERE expirationTime < :currentTime")
    suspend fun deleteExpiredMetadata(currentTime: Long)

    @Query("SELECT * FROM cache_metadata WHERE entityType = :entityType ORDER BY lastFetched DESC")
    suspend fun getCacheMetadataByType(entityType: String): List<CacheMetadata>
}