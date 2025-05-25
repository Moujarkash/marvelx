package com.mod.marvelx

import com.mod.marvelx.repositories.MarvelRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class Greeting: KoinComponent {
    private val repo by inject<MarvelRepository>()

    suspend fun greeting(): String {
        val charactersResponse = repo.getCharacters(offset = 0, limit = 40)
        val comicsResponse = repo.getComics(offset = 0, limit = 40)
        return charactersResponse.items[30].name + " - " + comicsResponse.items[30].title
    }
}