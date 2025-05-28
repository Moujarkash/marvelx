package com.mod.marvelx.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.mod.marvelx.LogLevel
import com.mod.marvelx.appLog
import com.mod.marvelx.models.Character
import com.mod.marvelx.models.ImageVariant
import com.mod.marvelx.ui.MarvelColors

@Composable
fun CharacterInfoSection(character: Character) {
    MarvelCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Character Image
                AsyncImage(
                    model = character.thumbnail.getImageUrl(variant = ImageVariant.STANDARD_XLARGE.value).replace("http://", "https://"),
                    contentDescription = character.name,
                    modifier = Modifier
                        .size(120.dp, 180.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop,
                    onError = { error ->
                        appLog(LogLevel.ERROR, "Failed to load character image: ${error.result.throwable}")
                    }
                )

                Spacer(modifier = Modifier.width(16.dp))

                // Character Info
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = character.name,
                        style = MaterialTheme.typography.headlineMedium,
                        color = MarvelColors.TextPrimary,
                        fontWeight = FontWeight.Bold
                    )

                    if (character.description.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = character.description,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MarvelColors.TextSecondary,
                            lineHeight = 20.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Resource Stats
                    CharacterStatsRow(character = character)
                }
            }

            // Character URLs (if available)
            if (character.urls.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                CharacterLinksSection(urls = character.urls)
            }
        }
    }
}