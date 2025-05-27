package com.mod.marvelx.repositories

import com.mod.marvelx.LogLevel
import com.mod.marvelx.appLog
import com.mod.marvelx.database.AppDatabase
import com.mod.marvelx.database.entities.CacheMetadata
import com.mod.marvelx.exceptions.NotModifiedException
import com.mod.marvelx.extensions.*
import com.mod.marvelx.models.Character
import com.mod.marvelx.models.Comic
import com.mod.marvelx.models.PaginatedResult
import com.mod.marvelx.services.MarvelApiService
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class MarvelRepository(
    private val apiService: MarvelApiService,
    appDatabase: AppDatabase
) {
    private val cacheMetadataDao = appDatabase.getCacheMetadataDao()
    private val characterDao = appDatabase.getCharacterDao()
    private val comicDao = appDatabase.getComicDao()

    @OptIn(ExperimentalTime::class, ExperimentalUuidApi::class)
    suspend fun getComics(
        offset: Int = 0,
        limit: Int = 20,
        titleStartsWith: String? = null,
        forceRefresh: Boolean = false
    ): PaginatedResult<Comic> {

        val requestKey = buildRequestKey("comic", offset, limit, titleStartsWith)
        val cacheMetadata = cacheMetadataDao.getCacheMetadata(requestKey)

        if (!forceRefresh && cacheMetadata != null && !isCacheExpired(cacheMetadata)) {
            val cachedComics = if (titleStartsWith != null) {
                comicDao.searchComics(titleStartsWith, offset, limit)
            } else {
                comicDao.getComics(offset, limit)
            }

            if (cachedComics.isNotEmpty()) {
                return PaginatedResult(
                    items = cachedComics.map { it.toDomain() },
                    offset = offset,
                    limit = limit,
                    total = cacheMetadata.total,
                    hasMore = offset + limit < cacheMetadata.total
                )
            }
        }

        // Make API request with ETag if available
        return try {
            val headers = cacheMetadata?.etag?.let { mapOf("If-None-Match" to it) } ?: emptyMap()
            val response = apiService.getComics(
                offset = offset,
                limit = limit,
                titleStartsWith = titleStartsWith,
                headers = headers
            )

            when (response.code) {
                304 -> {
                    // Not modified - throw exception to indicate no new data
                    throw NotModifiedException("Data not modified since last request. ETag: ${cacheMetadata?.etag}")
                }

                200 -> {
                    // New data, cache it
                    val comics = response.data.results
                    comicDao.insertComics(comics.map { it.toEntity() })

                    // Update cache metadata
                    val newMetadata = CacheMetadata(
                        id = Uuid.random().toString(),
                        requestKey = requestKey,
                        etag = response.etag,
                        lastFetched = Clock.System.now().toEpochMilliseconds(),
                        offset = offset,
                        limit = limit,
                        total = response.data.total,
                        entityType = "comic",
                        queryParams = titleStartsWith,
                        expirationTime = Clock.System.now().toEpochMilliseconds() + (24 * 60 * 60 * 1000)
                    )
                    cacheMetadataDao.insertCacheMetadata(newMetadata)

                    PaginatedResult(
                        items = comics,
                        offset = offset,
                        limit = limit,
                        total = response.data.total,
                        hasMore = offset + limit < response.data.total
                    )
                }

                else -> throw Exception("API Error: ${response.status}")
            }
        } catch (e: NotModifiedException) {
            // Handle 304 - return cached data if available
            appLog(LogLevel.DEBUG, "Data not modified: ${e.message}")

            val cachedComics = if (titleStartsWith != null) {
                comicDao.searchComics(titleStartsWith, offset, limit)
            } else {
                comicDao.getComics(offset, limit)
            }

            if (cachedComics.isNotEmpty() && cacheMetadata != null) {
                PaginatedResult(
                    items = cachedComics.map { it.toDomain() },
                    offset = offset,
                    limit = limit,
                    total = cacheMetadata.total,
                    hasMore = offset + limit < cacheMetadata.total
                )
            } else {
                // No cached data available, force a fresh request without ETag
                appLog(LogLevel.WARN, "No cached data available for 304 response, making fresh request")
                getComics(
                    offset = offset,
                    limit = limit,
                    titleStartsWith = titleStartsWith,
                    forceRefresh = true
                )
            }
        } catch (e: Exception) {
            appLog(LogLevel.ERROR, e.message ?: "Error fetching characters")

            // Fallback to cache on network error
            val cachedComics = if (titleStartsWith != null) {
                comicDao.searchComics(titleStartsWith, offset, limit)
            } else {
                comicDao.getComics(offset, limit)
            }

            if (cachedComics.isNotEmpty()) {
                PaginatedResult(
                    items = cachedComics.map { it.toDomain() },
                    offset = offset,
                    limit = limit,
                    total = cacheMetadata?.total ?: cachedComics.size,
                    hasMore = false // Conservative approach during offline
                )
            } else {
                throw e
            }
        }
    }

    @OptIn(ExperimentalTime::class, ExperimentalUuidApi::class)
    suspend fun getCharacters(
        offset: Int = 0,
        limit: Int = 20,
        nameStartsWith: String? = null,
        forceRefresh: Boolean = false
    ): PaginatedResult<Character> {

        val requestKey = buildRequestKey("character", offset, limit, nameStartsWith)
        val cacheMetadata = cacheMetadataDao.getCacheMetadata(requestKey)

        if (!forceRefresh && cacheMetadata != null && !isCacheExpired(cacheMetadata)) {
            val cachedCharacters = if (nameStartsWith != null) {
                characterDao.searchCharacters(nameStartsWith, offset, limit)
            } else {
                characterDao.getCharacters(offset, limit)
            }

            if (cachedCharacters.isNotEmpty()) {
                return PaginatedResult(
                    items = cachedCharacters.map { it.toDomain() },
                    offset = offset,
                    limit = limit,
                    total = cacheMetadata.total,
                    hasMore = offset + limit < cacheMetadata.total
                )
            }
        }

        // Make API request with ETag if available
        return try {
            val headers = cacheMetadata?.etag?.let { mapOf("If-None-Match" to it) } ?: emptyMap()
            val response = apiService.getCharacters(
                offset = offset,
                limit = limit,
                nameStartsWith = nameStartsWith,
                headers = headers
            )

            when (response.code) {
                304 -> {
                    // Not modified - throw exception to indicate no new data
                    throw NotModifiedException("Data not modified since last request. ETag: ${cacheMetadata?.etag}")
                }

                200 -> {
                    // New data, cache it
                    val characters = response.data.results
                    characterDao.insertCharacters(characters.map { it.toEntity() })

                    // Update cache metadata
                    val newMetadata = CacheMetadata(
                        id = Uuid.random().toString(),
                        requestKey = requestKey,
                        etag = response.etag,
                        lastFetched = Clock.System.now().toEpochMilliseconds(),
                        offset = offset,
                        limit = limit,
                        total = response.data.total,
                        entityType = "character",
                        queryParams = nameStartsWith,
                        expirationTime = Clock.System.now().toEpochMilliseconds() + (24 * 60 * 60 * 1000)
                    )
                    cacheMetadataDao.insertCacheMetadata(newMetadata)

                    PaginatedResult(
                        items = characters,
                        offset = offset,
                        limit = limit,
                        total = response.data.total,
                        hasMore = offset + limit < response.data.total
                    )
                }

                else -> throw Exception("API Error: ${response.status}")
            }
        } catch (e: NotModifiedException) {
            // Handle 304 - return cached data if available
            appLog(LogLevel.DEBUG, "Data not modified: ${e.message}")

            val cachedCharacters = if (nameStartsWith != null) {
                characterDao.searchCharacters(nameStartsWith, offset, limit)
            } else {
                characterDao.getCharacters(offset, limit)
            }

            if (cachedCharacters.isNotEmpty() && cacheMetadata != null) {
                PaginatedResult(
                    items = cachedCharacters.map { it.toDomain() },
                    offset = offset,
                    limit = limit,
                    total = cacheMetadata.total,
                    hasMore = offset + limit < cacheMetadata.total
                )
            } else {
                // No cached data available, force a fresh request without ETag
                appLog(LogLevel.WARN, "No cached data available for 304 response, making fresh request")
                getCharacters(
                    offset = offset,
                    limit = limit,
                    nameStartsWith = nameStartsWith,
                    forceRefresh = true
                )
            }
        } catch (e: Exception) {
            appLog(LogLevel.ERROR, e.message ?: "Error fetching characters")

            // Fallback to cache on network error
            val cachedCharacters = if (nameStartsWith != null) {
                characterDao.searchCharacters(nameStartsWith, offset, limit)
            } else {
                characterDao.getCharacters(offset, limit)
            }

            if (cachedCharacters.isNotEmpty()) {
                PaginatedResult(
                    items = cachedCharacters.map { it.toDomain() },
                    offset = offset,
                    limit = limit,
                    total = cacheMetadata?.total ?: cachedCharacters.size,
                    hasMore = false // Conservative approach during offline
                )
            } else {
                throw e
            }
        }
    }

    private fun buildRequestKey(
        entityType: String,
        offset: Int,
        limit: Int,
        query: String? = null
    ): String {
        return "${entityType}_offset_${offset}_limit_${limit}${query?.let { "_query_$it" } ?: ""}"
    }

    @OptIn(ExperimentalTime::class)
    private fun isCacheExpired(metadata: CacheMetadata): Boolean {
        return Clock.System.now().toEpochMilliseconds() > metadata.expirationTime
    }

    @OptIn(ExperimentalTime::class)
    suspend fun cleanupExpiredCache() {
        val currentTime = Clock.System.now().toEpochMilliseconds()
        val expiredThreshold = currentTime - (7 * 24 * 60 * 60 * 1000) // 7 days

        characterDao.deleteOldCharacters(expiredThreshold)
        comicDao.deleteOldComics(expiredThreshold)
        cacheMetadataDao.deleteExpiredMetadata(currentTime)
    }
}