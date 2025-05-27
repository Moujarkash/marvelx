package com.mod.marvelx.ui.components

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.mod.marvelx.LogLevel
import com.mod.marvelx.appLog
import com.mod.marvelx.models.Comic
import com.mod.marvelx.models.ImageVariant
import com.mod.marvelx.ui.MarvelColors

@Composable
fun ComicItem(
    comic: Comic,
    onClick: () -> Unit
) {
    MarvelCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(12.dp)
        ) {
            // Comic Image
            AsyncImage(
                model = comic.thumbnail.getImageUrl(variant = ImageVariant.PORTRAIT_MEDIUM.value).replace("http://", "https://"),
                contentDescription = comic.title,
                modifier = Modifier
                    .size(80.dp, 120.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop,
                onError = { error ->
                    appLog(LogLevel.ERROR, "Failed to load image: ${error.result.throwable}")
                }
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Comic Info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = comic.title,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MarvelColors.TextPrimary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                if (comic.description?.isNotEmpty() == true) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = comic.description,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MarvelColors.TextSecondary,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Issue and Page info
                Row {
                    if (comic.issueNumber > 0) {
                        ComicInfoChip(text = "Issue #${comic.issueNumber.toInt()}")
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    if (comic.pageCount > 0) {
                        ComicInfoChip(text = "${comic.pageCount} pages")
                    }
                }

                // Price (if available)
                comic.prices.firstOrNull { it.type == "printPrice" }?.let { price ->
                    if (price.price > 0) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${price.price}",
                            style = MaterialTheme.typography.titleMedium,
                            color = MarvelColors.MarvelGold
                        )
                    }
                }
            }
        }
    }
}