package com.azhua.core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.azhua.core.model.Donghua
import com.azhua.core.model.Episode
import com.azhua.core.ui.theme.*

@Composable
fun ContinueWatchingCard(
    donghua: Donghua,
    episode: Episode,
    progress: Float,
    timestamp: Long,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .background(ColorSurface)
            .clickable(onClick = onClick)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Thumbnail
        Box(
            modifier = Modifier
                .size(120.dp, 80.dp)
                .clip(MaterialTheme.shapes.small)
        ) {
            AsyncImage(
                model = donghua.coverUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )

            // Play overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(ColorScrimMedium),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Filled.PlayArrow,
                    contentDescription = "Play",
                    tint = ColorPrimary,
                    modifier = Modifier.size(36.dp),
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Info
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = donghua.title,
                style = MaterialTheme.typography.titleMedium,
                color = ColorTextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Text(
                text = "Ep ${episode.episodeNumber.toInt()}${episode.title?.let { " - $it" } ?: ""}",
                style = MaterialTheme.typography.bodySmall,
                color = ColorTextSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Progress bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .clip(MaterialTheme.shapes.extraSmall)
                    .background(ColorSurfaceVariant)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress)
                        .height(3.dp)
                        .clip(MaterialTheme.shapes.extraSmall)
                        .background(ColorSecondary)
                )
            }
        }
    }
}
