package com.mod.marvelx.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import com.mod.marvelx.ui.MarvelColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface

@Composable
fun ComicInfoChip(text: String) {
    Surface(
        color = MarvelColors.MarvelRed.copy(alpha = 0.1f),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = MarvelColors.MarvelRed,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}