package com.mod.marvelx.services

import com.mod.marvelx.exceptions.NotModifiedException
import com.mod.marvelx.models.Comic
import com.mod.marvelx.models.Character
import com.mod.marvelx.models.Creator
import com.mod.marvelx.models.Event
import com.mod.marvelx.models.MarvelApiResponse
import com.mod.marvelx.models.Series
import com.mod.marvelx.models.Story
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import kotlinx.coroutines.delay

class MarvelApiService(
    private val httpClient: HttpClient
) {
    companion object {
        private const val BASE_URL = "https://gateway.marvel.com/v1/public"
        private const val MAX_RETRIES = 3
        private const val RETRY_DELAY_MS = 1000L
    }

    suspend fun getCharacters(
        offset: Int,
        limit: Int,
        nameStartsWith: String? = null,
        orderBy: String? = null,
        headers: Map<String, String>
    ): MarvelApiResponse<Character> {
        return executeWithRetry {
            val response = httpClient.get("$BASE_URL/characters") {
                parameter("offset", offset)
                parameter("limit", limit.coerceAtMost(100))
                nameStartsWith?.let { parameter("nameStartsWith", it) }
                orderBy?.let { parameter("orderBy", it) }
                headers.forEach { (key, value) ->
                    header(key, value)
                }
            }

            if (response.status.value == 304) {
                throw NotModifiedException("Data not modified since last request")
            }

            response.body()
        }
    }

    suspend fun getCharacterById(
        id: Int,
        headers: Map<String, String>
    ): MarvelApiResponse<Character> {
        return executeWithRetry {
           val response = httpClient.get("$BASE_URL/characters/$id") {
                headers.forEach { (key, value) ->
                    header(key, value)
                }
            }

            if (response.status.value == 304) {
                throw NotModifiedException("Data not modified since last request")
            }

            response.body()
        }
    }

    suspend fun getCharacterComics(
        characterId: Int,
        offset: Int,
        limit: Int,
        orderBy: String? = null,
        headers: Map<String, String>
    ): MarvelApiResponse<Comic> {
        return executeWithRetry {
            val response = httpClient.get("$BASE_URL/characters/$characterId/comics") {
                parameter("offset", offset)
                parameter("limit", limit.coerceAtMost(100))
                orderBy?.let { parameter("orderBy", it) }
                headers.forEach { (key, value) ->
                    header(key, value)
                }
            }

            if (response.status.value == 304) {
                throw NotModifiedException("Data not modified since last request")
            }

            response.body()
        }
    }

    suspend fun getComics(
        offset: Int,
        limit: Int,
        titleStartsWith: String? = null,
        orderBy: String? = null,
        headers: Map<String, String>
    ): MarvelApiResponse<Comic> {
        return executeWithRetry {
            val response = httpClient.get("$BASE_URL/comics") {
                parameter("offset", offset)
                parameter("limit", limit.coerceAtMost(100))
                titleStartsWith?.let { parameter("titleStartsWith", it) }
                orderBy?.let { parameter("orderBy", it) }
                headers.forEach { (key, value) ->
                    header(key, value)
                }
            }

            if (response.status.value == 304) {
                throw NotModifiedException("Data not modified since last request")
            }

            response.body()
        }
    }

    suspend fun getComicById(
        id: Int,
        headers: Map<String, String>
    ): MarvelApiResponse<Comic> {
        return executeWithRetry {
            val response = httpClient.get("$BASE_URL/comics/$id") {
                headers.forEach { (key, value) ->
                    header(key, value)
                }
            }

            if (response.status.value == 304) {
                throw NotModifiedException("Data not modified since last request")
            }

            response.body()
        }
    }

    suspend fun getComicCharacters(
        comicId: Int,
        offset: Int,
        limit: Int,
        headers: Map<String, String>
    ): MarvelApiResponse<Character> {
        return executeWithRetry {
            httpClient.get("$BASE_URL/comics/$comicId/characters") {
                parameter("offset", offset)
                parameter("limit", limit.coerceAtMost(100))
                headers.forEach { (key, value) ->
                    header(key, value)
                }
            }.body()
        }
    }

    suspend fun getSeries(
        offset: Int,
        limit: Int,
        titleStartsWith: String?,
        orderBy: String?,
        headers: Map<String, String>
    ): MarvelApiResponse<Series> {
        return executeWithRetry {
            httpClient.get("$BASE_URL/series") {
                parameter("offset", offset)
                parameter("limit", limit.coerceAtMost(100))
                titleStartsWith?.let { parameter("titleStartsWith", it) }
                orderBy?.let { parameter("orderBy", it) }
                headers.forEach { (key, value) ->
                    header(key, value)
                }
            }.body()
        }
    }

    suspend fun getSeriesById(
        id: Int,
        headers: Map<String, String>
    ): MarvelApiResponse<Series> {
        return executeWithRetry {
            httpClient.get("$BASE_URL/series/$id") {
                headers.forEach { (key, value) ->
                    header(key, value)
                }
            }.body()
        }
    }

    suspend fun getStories(
        offset: Int,
        limit: Int,
        orderBy: String? = null,
        headers: Map<String, String>
    ): MarvelApiResponse<Story> {
        return executeWithRetry {
            httpClient.get("$BASE_URL/stories") {
                parameter("offset", offset)
                parameter("limit", limit.coerceAtMost(100))
                orderBy?.let { parameter("orderBy", it) }
                headers.forEach { (key, value) ->
                    header(key, value)
                }
            }.body()
        }
    }

    suspend fun getEvents(
        offset: Int,
        limit: Int,
        nameStartsWith: String? = null,
        orderBy: String? = null,
        headers: Map<String, String>
    ): MarvelApiResponse<Event> {
        return executeWithRetry {
            httpClient.get("$BASE_URL/events") {
                parameter("offset", offset)
                parameter("limit", limit.coerceAtMost(100))
                nameStartsWith?.let { parameter("nameStartsWith", it) }
                orderBy?.let { parameter("orderBy", it) }
                headers.forEach { (key, value) ->
                    header(key, value)
                }
            }.body()
        }
    }

    suspend fun getCreators(
        offset: Int,
        limit: Int,
        nameStartsWith: String? = null,
        orderBy: String? = null,
        headers: Map<String, String>
    ): MarvelApiResponse<Creator> {
        return executeWithRetry {
            httpClient.get("$BASE_URL/creators") {
                parameter("offset", offset)
                parameter("limit", limit.coerceAtMost(100))
                nameStartsWith?.let { parameter("nameStartsWith", it) }
                orderBy?.let { parameter("orderBy", it) }
                headers.forEach { (key, value) ->
                    header(key, value)
                }
            }.body()
        }
    }

    private suspend fun <T> executeWithRetry(
        maxRetries: Int = MAX_RETRIES,
        block: suspend () -> T
    ): T {
        var lastException: Exception? = null

        repeat(maxRetries) { attempt ->
            try {
                return block()
            } catch (e: Exception) {
                lastException = e

                // Handle specific HTTP status codes
                when {
                    e.message?.contains("429") == true -> {
                        // Rate limit exceeded - wait longer
                        val delayMs = RETRY_DELAY_MS * (attempt + 1) * 2 // Exponential backoff
                        delay(delayMs)
                    }
                    e.message?.contains("5") == true -> {
                        // Server error - retry with normal delay
                        delay(RETRY_DELAY_MS * (attempt + 1))
                    }
                    else -> {
                        // Other errors - don't retry
                        throw e
                    }
                }
            }
        }

        throw lastException ?: Exception("Unknown error after $maxRetries retries")
    }
}