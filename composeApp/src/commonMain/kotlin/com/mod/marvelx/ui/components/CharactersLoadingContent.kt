package com.mod.marvelx.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mod.marvelx.ui.MarvelColors

@Composable
fun CharactersLoadingContent() {
    MarvelCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                color = MarvelColors.MarvelRed,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Loading characters...",
                style = MaterialTheme.typography.bodyMedium,
                color = MarvelColors.TextSecondary
            )
        }
    }
}