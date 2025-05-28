package com.mod.marvelx.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mod.marvelx.ui.screens.CharacterDetailsScreen
import com.mod.marvelx.ui.screens.CharactersScreen
import com.mod.marvelx.ui.screens.ComicDetailsScreen
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
                onComicClick = { comicId, title ->
                    navController.navigate(Screen.ComicDetails.createRoute(comicId, title))
                }
            )
        }

        composable(Screen.Characters.route) {
            CharactersScreen(
                onCharacterClick = { characterId, characterName ->
                    navController.navigate(Screen.CharacterDetails.createRoute(characterId, characterName))
                }
            )
        }

        composable(Screen.ComicDetails.route) { backStackEntry ->
            val comicId = backStackEntry.arguments?.getString("comicId") ?: ""
            ComicDetailsScreen(
                comicId = comicId,
                onCharacterClick = { characterId, characterName ->
                    navController.navigate(Screen.CharacterDetails.createRoute(characterId, characterName))
                }
            )
        }

        composable(Screen.CharacterDetails.route) { backStackEntry ->
            val characterId = backStackEntry.arguments?.getString("characterId") ?: ""
            CharacterDetailsScreen(
                characterId = characterId,
                onComicClick = { id, title ->
                    navController.navigate(Screen.ComicDetails.createRoute(id, title))
                }
            )
        }
    }
}