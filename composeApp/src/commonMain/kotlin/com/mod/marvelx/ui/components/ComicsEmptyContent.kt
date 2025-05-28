package com.mod.marvelx.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
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
fun ComicsEmptyContent() {
    MarvelCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No comics available for this character",
                style = MaterialTheme.typography.bodyLarge,
                color = MarvelColors.TextSecondary,
                textAlign = TextAlign.Center
            )
        }
    }
}