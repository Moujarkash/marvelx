package com.mod.marvelx

import com.mod.marvelx.services.MarvelApiService
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class Greeting: KoinComponent {
    private val apiService by inject<MarvelApiService>()

    suspend fun greeting(): String {
        val response = apiService.getComics(offset = 0, limit = 40, headers = emptyMap())
        return response.data.results[0].title
    }
}