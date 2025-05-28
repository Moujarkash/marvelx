package com.mod.marvelx.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mod.marvelx.ui.MarvelColors
import marvelx.composeapp.generated.resources.Res
import marvelx.composeapp.generated.resources.character_icon
import org.jetbrains.compose.resources.painterResource

@Composable
fun CharactersEmptyContent() {
    MarvelCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(Res.drawable.character_icon),
                contentDescription = null,
                tint = MarvelColors.TextSecondary,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "No characters found",
                style = MaterialTheme.typography.titleMedium,
                color = MarvelColors.TextSecondary,
                textAlign = TextAlign.Center
            )
            Text(
                text = "This comic doesn't have any associated characters.",
                style = MaterialTheme.typography.bodyMedium,
                color = MarvelColors.TextSecondary,
                textAlign = TextAlign.Center
            )
        }
    }
}