package com.mod.marvelx.ui

sealed class Screen(val route: String, val title: String) {
    data object Comics : Screen("comics", "Comics")
    data object Characters : Screen("characters", "Characters")
    data object ComicDetails : Screen("comic_detail/{comicId}?title={title}", "Comic Details") {
        fun createRoute(comicId: String, title: String? = null) = "comic_detail/$comicId?title=$title"
    }
    data object CharacterDetails : Screen("character_detail/{characterId}?title={title}", "Character Details") {
        fun createRoute(characterId: String, title: String? = null) = "character_detail/$characterId?title=$title"
    }
}