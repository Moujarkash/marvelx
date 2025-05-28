package com.mod.marvelx.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mod.marvelx.models.ComicDate
import com.mod.marvelx.ui.MarvelColors
import com.mod.marvelx.utils.formatDate

@Composable
fun ComicDateChip(date: ComicDate) {
    Surface(
        color = MarvelColors.MarvelRed.copy(alpha = 0.1f),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text(
                text = date.type.replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.bodySmall,
                color = MarvelColors.MarvelRed,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = formatDate(date.date),
                style = MaterialTheme.typography.bodySmall,
                color = MarvelColors.TextSecondary
            )
        }
    }
}