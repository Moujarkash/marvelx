package com.mod.marvelx.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mod.marvelx.database.entities.ComicEntity

@Dao
interface ComicDao {
    @Query("SELECT * FROM comics ORDER BY title ASC LIMIT :limit OFFSET :offset")
    suspend fun getComics(offset: Int, limit: Int): List<ComicEntity>

    @Query("SELECT * FROM comics WHERE id = :id")
    suspend fun getComicById(id: Int): ComicEntity?

    @Query("SELECT * FROM comics WHERE title LIKE '%' || :query || '%' ORDER BY title ASC LIMIT :limit OFFSET :offset")
    suspend fun searchComics(query: String, offset: Int, limit: Int): List<ComicEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComics(comics: List<ComicEntity>)

    @Query("DELETE FROM comics WHERE lastUpdated < :timestamp")
    suspend fun deleteOldComics(timestamp: Long)
}