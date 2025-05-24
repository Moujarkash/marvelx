package com.mod.marvelx

import com.mod.marvelx.models.Comic
import com.mod.marvelx.models.MarvelApiResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class Greeting: KoinComponent {
    private val platform = getPlatform()
    private val client by inject<HttpClient>()

    fun greet(): String {
        return "Hello, ${platform.name}!"
    }

    suspend fun greeting(): String {
        val response = client.get("https://gateway.marvel.com/v1/public/comics/")
        val comicResponse = response.body<MarvelApiResponse<Comic>>()
        return comicResponse.data.results[0].title
    }
}