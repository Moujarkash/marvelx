package com.mod.marvelx.di

import com.mod.marvelx.BuildKonfig
import com.mod.marvelx.Greeting
import com.mod.marvelx.managers.ApiKeyManager
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.compression.ContentEncoding
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.plugin
import io.ktor.client.request.parameter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
val appModule = module {
    singleOf(::ApiKeyManager)
    single {
        HttpClient {
            install(Logging) {
                logger = object: Logger {
                    override fun log(message: String) {
                        Napier.v("HTTP Client", null, message)
                    }
                }
                level = LogLevel.HEADERS
            }.also { Napier.base(DebugAntilog()) }

            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                    coerceInputValues = true
                })
            }
            install(ContentEncoding) {
                gzip()
            }
        }.also {
            val apiKeyManager = get<ApiKeyManager>()
            it.plugin(HttpSend).intercept { request ->
                apiKeyManager.storeApiKeys(apiKey = BuildKonfig.API_KEY, privateKey = BuildKonfig.PRIVATE_KEY)

                val timestamp = kotlin.time.Clock.System.now().toEpochMilliseconds().toString()
                val apiKey =  apiKeyManager.getApiKey() ?: ""
                val hash = apiKeyManager.generateHash(timestamp)

                if (hash != null) {
                    request.parameter("ts", timestamp)
                    request.parameter("hash", hash)
                    request.parameter("apikey", apiKey)
                }
                execute(request)
            }
        }
    }
    singleOf(::Greeting)
}