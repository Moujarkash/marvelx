package com.mod.marvelx.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mod.marvelx.ui.MarvelColors
import com.mod.marvelx.ui.Screen
import marvelx.composeapp.generated.resources.Res
import marvelx.composeapp.generated.resources.comic_icon
import marvelx.composeapp.generated.resources.character_icon
import org.jetbrains.compose.resources.painterResource

@Composable
fun DrawerContent(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        // Drawer Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(MarvelColors.MarvelRed),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "MARVEL",
                style = MaterialTheme.typography.headlineLarge,
                color = MarvelColors.TextPrimary
            )
        }

        HorizontalDivider(color = MarvelColors.MarvelGray)

        // Navigation Items
        DrawerItem(
            icon = painterResource(Res.drawable.comic_icon),
            title = "Comics",
            isSelected = currentRoute == Screen.Comics.route,
            onClick = { onNavigate(Screen.Comics.route) }
        )

        DrawerItem(
            icon = painterResource(Res.drawable.character_icon),
            title = "Characters",
            isSelected = currentRoute == Screen.Characters.route,
            onClick = { onNavigate(Screen.Characters.route) }
        )
    }
}