package com.mod.marvelx.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mod.marvelx.ui.screens.CharacterDetailScreen
import com.mod.marvelx.ui.screens.CharactersScreen
import com.mod.marvelx.ui.screens.ComicDetailScreen
import com.mod.marvelx.ui.screens.ComicsScreen

@Composable
fun MarvelNavigation(
    navController: NavHostController,
    startDestination: String = Screen.Comics.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Comics.route) {
            ComicsScreen(
                onComicClick = { comicId ->
                    navController.navigate(Screen.ComicDetail.createRoute(comicId))
                }
            )
        }

        composable(Screen.Characters.route) {
            CharactersScreen(
                onCharacterClick = { characterId ->
                    navController.navigate(Screen.CharacterDetail.createRoute(characterId))
                }
            )
        }

        composable(Screen.ComicDetail.route) { backStackEntry ->
            val comicId = backStackEntry.arguments?.getString("comicId") ?: ""
            ComicDetailScreen(
                comicId = comicId,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.CharacterDetail.route) { backStackEntry ->
            val characterId = backStackEntry.arguments?.getString("characterId") ?: ""
            CharacterDetailScreen(
                characterId = characterId,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}