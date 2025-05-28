package com.mod.marvelx.repositories

import com.mod.marvelx.LogLevel
import com.mod.marvelx.appLog
import com.mod.marvelx.database.AppDatabase
import com.mod.marvelx.database.entities.CacheMetadata
import com.mod.marvelx.database.entities.CharacterComicCrossRef
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

    @OptIn(ExperimentalTime::class, ExperimentalUuidApi::class)
    suspend fun getCharacterComics(
        characterId: Int,
        offset: Int = 0,
        limit: Int = 20,
        orderBy: String? = null,
        forceRefresh: Boolean = false
    ): PaginatedResult<Comic> {

        val requestKey = buildRequestKey("character_comics", offset, limit, "${characterId}_${orderBy ?: ""}")
        val cacheMetadata = cacheMetadataDao.getCacheMetadata(requestKey)

        if (!forceRefresh && cacheMetadata != null && !isCacheExpired(cacheMetadata)) {
            val cachedComics = comicDao.getComicsByCharacterId(characterId, offset, limit)

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
            val response = apiService.getCharacterComics(
                characterId = characterId,
                offset = offset,
                limit = limit,
                orderBy = orderBy,
                headers = headers
            )

            when (response.code) {
                304 -> {
                    // Not modified - throw exception to indicate no new data
                    throw NotModifiedException("Character comics not modified since last request. ETag: ${cacheMetadata?.etag}")
                }

                200 -> {
                    // New data, cache it
                    val comics = response.data.results

                    // Store comics
                    comicDao.insertComics(comics.map { it.toEntity() })

                    // Store character-comic relationships
                    comicDao.insertCharacterComicCrossRefs(
                        comics.map { CharacterComicCrossRef(characterId = characterId, comicId = it.id) }
                    )

                    // Update cache metadata
                    val newMetadata = CacheMetadata(
                        id = Uuid.random().toString(),
                        requestKey = requestKey,
                        etag = response.etag,
                        lastFetched = Clock.System.now().toEpochMilliseconds(),
                        offset = offset,
                        limit = limit,
                        total = response.data.total,
                        entityType = "character_comics",
                        queryParams = "${characterId}_${orderBy ?: ""}",
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
            appLog(LogLevel.DEBUG, "Character comics not modified: ${e.message}")

            val cachedComics = comicDao.getComicsByCharacterId(characterId, offset, limit)

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
                appLog(LogLevel.WARN, "No cached character comics available for 304 response, making fresh request")
                getCharacterComics(
                    characterId = characterId,
                    offset = offset,
                    limit = limit,
                    orderBy = orderBy,
                    forceRefresh = true
                )
            }
        } catch (e: Exception) {
            appLog(LogLevel.ERROR, e.message ?: "Error fetching comics for character $characterId")

            // Fallback to cache on network error
            val cachedComics = comicDao.getComicsByCharacterId(characterId, offset, limit)

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
    suspend fun getComicCharacters(
        comicId: Int,
        offset: Int = 0,
        limit: Int = 20,
        forceRefresh: Boolean = false
    ): PaginatedResult<Character> {

        val requestKey = buildRequestKey("comic_characters", offset, limit, "$comicId")
        val cacheMetadata = cacheMetadataDao.getCacheMetadata(requestKey)

        if (!forceRefresh && cacheMetadata != null && !isCacheExpired(cacheMetadata)) {
            val cachedCharacters = characterDao.getCharactersByComicId(comicId, offset, limit)

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
            val response = apiService.getComicCharacters(
                comicId = comicId,
                offset = offset,
                limit = limit,
                headers = headers
            )

            when (response.code) {
                304 -> {
                    // Not modified - throw exception to indicate no new data
                    throw NotModifiedException("Comic Characters not modified since last request. ETag: ${cacheMetadata?.etag}")
                }

                200 -> {
                    // New data, cache it
                    val characters = response.data.results

                    // Store comics
                    characterDao.insertCharacters(characters.map { it.toEntity() })

                    // Store character-comic relationships
                    characterDao.insertCharacterComicCrossRefs(
                        characters.map { CharacterComicCrossRef(comicId = comicId, characterId = it.id) }
                    )

                    // Update cache metadata
                    val newMetadata = CacheMetadata(
                        id = Uuid.random().toString(),
                        requestKey = requestKey,
                        etag = response.etag,
                        lastFetched = Clock.System.now().toEpochMilliseconds(),
                        offset = offset,
                        limit = limit,
                        total = response.data.total,
                        entityType = "comic_characters",
                        queryParams = "$comicId",
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
            appLog(LogLevel.DEBUG, "Comic Characters not modified: ${e.message}")

            val cachedCharacters = characterDao.getCharactersByComicId(comicId, offset, limit)

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
                appLog(LogLevel.WARN, "No cached comic characters available for 304 response, making fresh request")
                getComicCharacters(
                    comicId = comicId,
                    offset = offset,
                    limit = limit,
                    forceRefresh = true
                )
            }
        } catch (e: Exception) {
            appLog(LogLevel.ERROR, e.message ?: "Error fetching characters for comic $comicId")

            // Fallback to cache on network error
            val cachedCharacters = characterDao.getCharactersByComicId(comicId, offset, limit)

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

    @OptIn(ExperimentalTime::class, ExperimentalUuidApi::class)
    suspend fun getCharacterById(
        id: Int,
        forceRefresh: Boolean = false
    ): Character {
        val requestKey = "character_id_$id"
        val cacheMetadata = cacheMetadataDao.getCacheMetadata(requestKey)

        // Check cache first
        if (!forceRefresh && cacheMetadata != null && !isCacheExpired(cacheMetadata)) {
            val cachedCharacter = characterDao.getCharacterById(id)
            if (cachedCharacter != null) {
                return cachedCharacter.toDomain()
            }
        }

        // Make API request with ETag if available
        return try {
            val headers = cacheMetadata?.etag?.let { mapOf("If-None-Match" to it) } ?: emptyMap()
            val response = apiService.getCharacterById(id, headers)

            when (response.code) {
                304 -> {
                    // Not modified - throw exception to indicate no new data
                    throw NotModifiedException("Character not modified since last request. ETag: ${cacheMetadata?.etag}")
                }

                200 -> {
                    // New data, cache it
                    val character = response.data.results.firstOrNull()
                        ?: throw Exception("Character with ID $id not found")

                    characterDao.insertCharacters(listOf(character.toEntity()))

                    // Update cache metadata
                    val newMetadata = CacheMetadata(
                        id = Uuid.random().toString(),
                        requestKey = requestKey,
                        etag = response.etag,
                        lastFetched = Clock.System.now().toEpochMilliseconds(),
                        offset = 0,
                        limit = 1,
                        total = 1,
                        entityType = "character",
                        queryParams = id.toString(),
                        expirationTime = Clock.System.now().toEpochMilliseconds() + (24 * 60 * 60 * 1000)
                    )
                    cacheMetadataDao.insertCacheMetadata(newMetadata)

                    character
                }

                else -> throw Exception("API Error: ${response.status}")
            }
        } catch (e: NotModifiedException) {
            // Handle 304 - return cached data if available
            appLog(LogLevel.DEBUG, "Character not modified: ${e.message}")

            val cachedCharacter = characterDao.getCharacterById(id)
            if (cachedCharacter != null && cacheMetadata != null) {
                cachedCharacter.toDomain()
            } else {
                // No cached data available, force a fresh request without ETag
                appLog(LogLevel.WARN, "No cached character available for 304 response, making fresh request")
                getCharacterById(id, forceRefresh = true)
            }
        } catch (e: Exception) {
            appLog(LogLevel.ERROR, e.message ?: "Error fetching character with ID $id")

            // Fallback to cache on network error
            val cachedCharacter = characterDao.getCharacterById(id)
            cachedCharacter?.toDomain() ?: throw e
        }
    }

    @OptIn(ExperimentalTime::class, ExperimentalUuidApi::class)
    suspend fun getComicById(
        id: Int,
        forceRefresh: Boolean = false
    ): Comic {
        val requestKey = "comic_id_$id"
        val cacheMetadata = cacheMetadataDao.getCacheMetadata(requestKey)

        // Check cache first
        if (!forceRefresh && cacheMetadata != null && !isCacheExpired(cacheMetadata)) {
            val cachedComic = comicDao.getComicById(id)
            if (cachedComic != null) {
                return cachedComic.toDomain()
            }
        }

        // Make API request with ETag if available
        return try {
            val headers = cacheMetadata?.etag?.let { mapOf("If-None-Match" to it) } ?: emptyMap()
            val response = apiService.getComicById(id, headers)

            when (response.code) {
                304 -> {
                    // Not modified - throw exception to indicate no new data
                    throw NotModifiedException("Comic not modified since last request. ETag: ${cacheMetadata?.etag}")
                }

                200 -> {
                    // New data, cache it
                    val comic = response.data.results.firstOrNull()
                        ?: throw Exception("Comic with ID $id not found")

                    comicDao.insertComics(listOf(comic.toEntity()))

                    // Update cache metadata
                    val newMetadata = CacheMetadata(
                        id = Uuid.random().toString(),
                        requestKey = requestKey,
                        etag = response.etag,
                        lastFetched = Clock.System.now().toEpochMilliseconds(),
                        offset = 0,
                        limit = 1,
                        total = 1,
                        entityType = "comic",
                        queryParams = id.toString(),
                        expirationTime = Clock.System.now().toEpochMilliseconds() + (24 * 60 * 60 * 1000)
                    )
                    cacheMetadataDao.insertCacheMetadata(newMetadata)

                    comic
                }

                else -> throw Exception("API Error: ${response.status}")
            }
        } catch (e: NotModifiedException) {
            // Handle 304 - return cached data if available
            appLog(LogLevel.DEBUG, "Comic not modified: ${e.message}")

            val cachedComic = comicDao.getComicById(id)
            if (cachedComic != null && cacheMetadata != null) {
                cachedComic.toDomain()
            } else {
                // No cached data available, force a fresh request without ETag
                appLog(LogLevel.WARN, "No cached comic available for 304 response, making fresh request")
                getComicById(id, forceRefresh = true)
            }
        } catch (e: Exception) {
            appLog(LogLevel.ERROR, e.message ?: "Error fetching comic with ID $id")

            // Fallback to cache on network error
            val cachedComic = comicDao.getComicById(id)
            cachedComic?.toDomain() ?: throw e
        }
    }
}