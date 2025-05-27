package com.mod.marvelx.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mod.marvelx.ui.MarvelColors
import com.mod.marvelx.ui.Screen

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

        Divider(color = MarvelColors.MarvelGray)

        // Navigation Items
        DrawerItem(
            icon = Icons.Default.Done,
            title = "Comics",
            isSelected = currentRoute == Screen.Comics.route,
            onClick = { onNavigate(Screen.Comics.route) }
        )

        DrawerItem(
            icon = Icons.Default.Person,
            title = "Characters",
            isSelected = currentRoute == Screen.Characters.route,
            onClick = { onNavigate(Screen.Characters.route) }
        )
    }
}