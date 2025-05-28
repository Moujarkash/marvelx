package com.mod.marvelx.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.mod.marvelx.LogLevel
import com.mod.marvelx.appLog
import com.mod.marvelx.models.Comic
import com.mod.marvelx.models.ImageVariant
import com.mod.marvelx.ui.MarvelColors

@Composable
fun ComicInfoSection(comic: Comic) {
    MarvelCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Comic Cover Image
                AsyncImage(
                    model = comic.thumbnail.getImageUrl(variant = ImageVariant.PORTRAIT_UNCANNY.value).replace("http://", "https://"),
                    contentDescription = comic.title,
                    modifier = Modifier
                        .width(120.dp)
                        .height(180.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop,
                    onError = { error ->
                        appLog(LogLevel.ERROR, "Failed to load comic image: ${error.result.throwable}")
                    }
                )

                // Comic Details
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    // Comic Title
                    Text(
                        text = comic.title,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MarvelColors.TextPrimary,
                        fontWeight = FontWeight.Bold,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Series
                    Text(
                        text = "Series: ${comic.series.name}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MarvelColors.TextSecondary
                    )

                    // Issue Number
                    if (comic.issueNumber > 0) {
                        Text(
                            text = "Issue #${comic.issueNumber.toInt()}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MarvelColors.TextSecondary
                        )
                    }

                    // Format and Page Count
                    if (comic.format.isNotEmpty()) {
                        Text(
                            text = comic.format + if (comic.pageCount > 0) " • ${comic.pageCount} pages" else "",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MarvelColors.TextSecondary
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Resource counts
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        if (comic.characters.available > 0) {
                            item {
                                InfoChip(
                                    text = "${comic.characters.available} characters",
                                    textModifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        if (comic.creators.available > 0) {
                            item {
                                InfoChip(
                                    text = "${comic.creators.available} creators",
                                    textModifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        if (comic.stories.available > 0) {
                            item {
                                InfoChip(
                                    text = "${comic.stories.available} stories",
                                    textModifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Comic Description
            if (!comic.description.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Description",
                    style = MaterialTheme.typography.titleMedium,
                    color = MarvelColors.TextPrimary,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = comic.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MarvelColors.TextSecondary,
                    lineHeight = 20.sp
                )
            }

            // Publication Dates
            if (comic.dates.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                ComicDatesSection(dates = comic.dates)
            }

            // Prices
            if (comic.prices.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                ComicPricesSection(prices = comic.prices)
            }

            // Links
            if (comic.urls.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                LinksSection(urls = comic.urls)
            }
        }
    }
}