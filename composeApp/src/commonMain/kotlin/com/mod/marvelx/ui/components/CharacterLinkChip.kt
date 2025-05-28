package com.mod.marvelx.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mod.marvelx.LogLevel
import com.mod.marvelx.appLog
import com.mod.marvelx.models.Url
import com.mod.marvelx.ui.MarvelColors
import com.mod.marvelx.utils.getPlatformContext
import com.mod.marvelx.utils.openUrl

@Composable
fun CharacterLinkChip(url: Url) {
    val context = getPlatformContext()

    Surface (
        color = MarvelColors.MarvelRed.copy(alpha = 0.1f),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.clickable {
            try {
                openUrl(url.url, context)
            } catch (e: Exception) {
                appLog(LogLevel.ERROR, "Failed to open URL: ${e.message}")
            }
        }
    ) {
        Text(
            text = url.type.replaceFirstChar { it.uppercase() },
            style = MaterialTheme.typography.bodySmall,
            color = MarvelColors.MarvelRed,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}