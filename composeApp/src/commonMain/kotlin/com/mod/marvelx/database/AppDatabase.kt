package com.mod.marvelx.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import com.mod.marvelx.database.dao.CacheMetadataDao
import com.mod.marvelx.database.dao.CharacterDao
import com.mod.marvelx.database.dao.ComicDao
import com.mod.marvelx.database.entities.CacheMetadata
import com.mod.marvelx.database.entities.CharacterEntity
import com.mod.marvelx.database.entities.ComicEntity

@Database(entities = [CharacterEntity::class, ComicEntity::class, CacheMetadata::class], version = 1)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getCharacterDao(): CharacterDao
    abstract fun getComicDao(): ComicDao
    abstract fun getCacheMetadataDao(): CacheMetadataDao
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}