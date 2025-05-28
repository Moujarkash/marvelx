package com.mod.marvelx.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import com.mod.marvelx.ui.MarvelColors

@Composable
fun CharactersSectionHeader(
    charactersCount: Int,
    isLoading: Boolean,
    onRefresh: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Characters ($charactersCount)",
            style = MaterialTheme.typography.titleLarge,
            color = MarvelColors.TextPrimary,
            fontWeight = FontWeight.Bold
        )

        IconButton(
            onClick = onRefresh,
            enabled = !isLoading
        ) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = "Refresh characters",
                tint = if (isLoading) MarvelColors.TextSecondary else MarvelColors.MarvelRed,
                modifier = if (isLoading) Modifier.rotate(360f) else Modifier
            )
        }
    }
}