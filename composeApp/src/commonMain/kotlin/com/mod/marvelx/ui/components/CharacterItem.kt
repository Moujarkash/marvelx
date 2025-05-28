package com.mod.marvelx.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.mod.marvelx.LogLevel
import com.mod.marvelx.appLog
import com.mod.marvelx.models.Character
import com.mod.marvelx.models.ImageVariant
import com.mod.marvelx.ui.MarvelColors

@Composable
fun CharacterItem(
    character: Character,
    imageVariant: ImageVariant = ImageVariant.STANDARD_LARGE,
    onClick: () -> Unit
) {
    MarvelCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            // Character Image
            AsyncImage(
                model = character.thumbnail.getImageUrl(variant = imageVariant.value).replace("http://", "https://"),
                contentDescription = character.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop,
                onError = { error ->
                    appLog(LogLevel.ERROR, "Failed to load image: ${error.result.throwable}")
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Character Name
            Text(
                text = character.name,
                style = MaterialTheme.typography.headlineSmall,
                color = MarvelColors.TextPrimary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            // Character Description (if available)
            if (character.description.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = character.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MarvelColors.TextSecondary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Resource counts
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                if (character.comics.available > 0) {
                    item {
                        InfoChip(text = "${character.comics.available} comics", textModifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                    }
                }
                if (character.series.available > 0) {
                    item {
                        InfoChip(text = "${character.series.available} series", textModifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                    }
                }
                if (character.events.available > 0) {
                    item {
                        InfoChip(text = "${character.events.available} events", textModifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                    }
                }
            }
        }
    }
}