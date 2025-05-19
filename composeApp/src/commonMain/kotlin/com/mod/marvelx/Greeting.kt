package com.mod.marvelx

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class Greeting: KoinComponent {
    private val platform = getPlatform()
    private val client by inject<HttpClient>()

    fun greet(): String {
        return "Hello, ${platform.name}!"
    }

    suspend fun greeting(): String {
        val response = client.get("https://ktor.io/docs/")
        return response.bodyAsText()
    }
}