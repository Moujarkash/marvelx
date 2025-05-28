package com.mod.marvelx.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mod.marvelx.models.ComicDate
import com.mod.marvelx.ui.MarvelColors

@Composable
fun ComicDatesSection(dates: List<ComicDate>) {
    Column {
        Text(
            text = "Publication Dates",
            style = MaterialTheme.typography.titleMedium,
            color = MarvelColors.TextPrimary,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(dates) { date ->
                ComicDateChip(date = date)
            }
        }
    }
}