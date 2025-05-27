package com.mod.marvelx.ui

sealed class Screen(val route: String, val title: String) {
    data object Comics : Screen("comics", "Comics")
    data object Characters : Screen("characters", "Characters")
    data object ComicDetail : Screen("comic_detail/{comicId}", "Comic Details") {
        fun createRoute(comicId: String) = "comic_detail/$comicId"
    }
    data object CharacterDetail : Screen("character_detail/{characterId}", "Character Details") {
        fun createRoute(characterId: String) = "character_detail/$characterId"
    }
}