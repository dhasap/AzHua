package com.azhua.core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.DownloadDone
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.azhua.core.model.Episode
import com.azhua.core.ui.theme.*

@Composable
fun EpisodeItem(
    episode: Episode,
    isWatched: Boolean,
    isDownloaded: Boolean,
    watchProgress: Float = 0f,
    onClick: () -> Unit,
    onLongClick: (() -> Unit)? = null,
    onDownloadClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Episode number badge
            Surface(
                shape = MaterialTheme.shapes.small,
                color = ColorPrimaryContainer,
                modifier = Modifier.padding(end = 12.dp),
            ) {
                Text(
                    text = String.format("%02d", episode.episodeNumber.toInt()),
                    style = MaterialTheme.typography.labelMedium,
                    color = ColorOnPrimaryContainer,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                )
            }

            // Title & meta
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = episode.title ?: "Episode ${episode.episodeNumber.toInt()}",
                    style = MaterialTheme.typography.titleMedium,
                    color = if (isWatched) ColorTextTertiary else ColorTextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                val meta = listOfNotNull(
                    if (episode.durationMs > 0) episode.formattedDuration else null,
                    if (episode.dateUpload > 0) {
                        val sdf = java.text.SimpleDateFormat("dd MMM yyyy", java.util.Locale.getDefault())
                        sdf.format(java.util.Date(episode.dateUpload))
                    } else null,
                ).joinToString(" · ")
                if (meta.isNotEmpty()) {
                    Text(
                        text = meta,
                        style = MaterialTheme.typography.bodySmall,
                        color = ColorTextSecondary,
                    )
                }
            }

            // Download icon
            IconButton(onClick = { onDownloadClick?.invoke() }) {
                Icon(
                    imageVector = if (isDownloaded) Icons.Filled.DownloadDone else Icons.Outlined.Download,
                    contentDescription = if (isDownloaded) "Sudah didownload" else "Download",
                    tint = if (isDownloaded) ColorSecondary else ColorIconDefault,
                    modifier = Modifier.size(20.dp),
                )
            }
        }

        // Watch progress bar
        if (watchProgress > 0f) {
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .clip(MaterialTheme.shapes.extraSmall)
                    .background(ColorSurfaceVariant)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(watchProgress)
                        .height(2.dp)
                        .clip(MaterialTheme.shapes.extraSmall)
                        .background(ColorSecondary)
                )
            }
        }

        // Divider
        HorizontalDivider(
            modifier = Modifier.padding(top = 8.dp),
            color = ColorSurfaceVariant.copy(alpha = 0.5f),
            thickness = 0.5.dp,
        )
    }
}
