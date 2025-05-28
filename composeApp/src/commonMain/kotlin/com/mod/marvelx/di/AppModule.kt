package com.mod.marvelx.di

import androidx.room.RoomDatabase
import com.mod.marvelx.BuildKonfig
import com.mod.marvelx.Greeting
import com.mod.marvelx.LogLevel
import com.mod.marvelx.database.AppDatabase
import com.mod.marvelx.database.getRoomDatabase
import com.mod.marvelx.managers.ApiKeyManager
import com.mod.marvelx.appLog
import com.mod.marvelx.repositories.MarvelRepository
import com.mod.marvelx.services.MarvelApiService
import com.mod.marvelx.viewModels.CharacterDetailsViewModel
import com.mod.marvelx.viewModels.CharactersViewModel
import com.mod.marvelx.viewModels.ComicsViewModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.compression.ContentEncoding
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.plugin
import io.ktor.client.request.parameter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
fun appModule(builder: RoomDatabase.Builder<AppDatabase>) = module {
    singleOf(::ApiKeyManager)
    single<HttpClient> {
        HttpClient {
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        appLog(LogLevel.VERBOSE, message)
                    }
                }
            }

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
                apiKeyManager.storeApiKeys(
                    apiKey = BuildKonfig.API_KEY,
                    privateKey = BuildKonfig.PRIVATE_KEY
                )

                val timestamp = Clock.System.now().toEpochMilliseconds().toString()
                val apiKey = apiKeyManager.getApiKey() ?: ""
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
    single<AppDatabase> { getRoomDatabase(builder) }
    singleOf(::MarvelApiService)
    singleOf(::MarvelRepository)
    viewModel { ComicsViewModel(get()) }
    viewModel { CharactersViewModel(get()) }
    viewModel { (characterId: Int) ->
        CharacterDetailsViewModel(
            marvelRepository = get(),
            characterId = characterId
        )
    }
}