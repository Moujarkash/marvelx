package com.mod.marvelx.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mod.marvelx.ui.MarvelColors

@Composable
fun CharactersErrorContent(
    error: String,
    onRetry: () -> Unit
) {
    MarvelCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Failed to load characters",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = error,
                style = MaterialTheme.typography.bodyMedium,
                color = MarvelColors.TextSecondary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(12.dp))
            MarvelButton(
                onClick = onRetry,
                text = "Retry"
            )
        }
    }
}
