package com.mod.marvelx.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.mod.marvelx.ui.MarvelColors

@Composable
fun CharacterStatItem(
    label: String,
    count: Int,
    icon: Painter
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            painter = icon,
            contentDescription = label,
            tint = MarvelColors.MarvelRed,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = "$count $label",
            style = MaterialTheme.typography.bodyMedium,
            color = MarvelColors.TextSecondary
        )
    }
}