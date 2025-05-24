package com.mod.marvelx.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mod.marvelx.database.entities.CharacterEntity

@Dao
interface CharacterDao {
    @Query("SELECT * FROM characters ORDER BY name ASC LIMIT :limit OFFSET :offset")
    suspend fun getCharacters(offset: Int, limit: Int): List<CharacterEntity>

    @Query("SELECT * FROM characters WHERE id = :id")
    suspend fun getCharacterById(id: Int): CharacterEntity?

    @Query("SELECT * FROM characters WHERE name LIKE '%' || :query || '%' ORDER BY name ASC LIMIT :limit OFFSET :offset")
    suspend fun searchCharacters(query: String, offset: Int, limit: Int): List<CharacterEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacters(characters: List<CharacterEntity>)

    @Query("DELETE FROM characters WHERE lastUpdated < :timestamp")
    suspend fun deleteOldCharacters(timestamp: Long)
}