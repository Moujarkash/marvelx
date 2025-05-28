package com.mod.marvelx.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.mod.marvelx.models.Character
import marvelx.composeapp.generated.resources.Res
import marvelx.composeapp.generated.resources.events_icon
import marvelx.composeapp.generated.resources.comic_icon
import marvelx.composeapp.generated.resources.series_icon
import marvelx.composeapp.generated.resources.stories_icon
import org.jetbrains.compose.resources.painterResource

@Composable
fun CharacterStatsRow(character: Character) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (character.comics.available > 0) {
            CharacterStatItem(
                label = "Comics",
                count = character.comics.available,
                icon =  painterResource(Res.drawable.comic_icon)
            )
        }
        if (character.series.available > 0) {
            CharacterStatItem(
                label = "Series",
                count = character.series.available,
                icon = painterResource(Res.drawable.series_icon)
            )
        }
        if (character.events.available > 0) {
            CharacterStatItem(
                label = "Events",
                count = character.events.available,
                icon = painterResource(Res.drawable.events_icon)
            )
        }
        if (character.stories.available > 0) {
            CharacterStatItem(
                label = "Stories",
                count = character.stories.available,
                icon = painterResource(Res.drawable.stories_icon)
            )
        }
    }
}