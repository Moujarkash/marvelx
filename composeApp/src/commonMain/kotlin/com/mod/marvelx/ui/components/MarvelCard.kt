package com.mod.marvelx.ui.components

import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.mod.marvelx.ui.MarvelColors

@Composable
fun MarvelCard(
    modifier: androidx.compose.ui.Modifier = androidx.compose.ui.Modifier,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MarvelColors.SurfaceCard
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            MarvelColors.MarvelRed.copy(alpha = 0.3f)
        ),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        content()
    }
}