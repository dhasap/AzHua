package com.azhua.feature.recents

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.WatchLater
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.azhua.core.ui.component.*
import com.azhua.core.ui.theme.*
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecentsScreen(
    onNavigateToDetail: (Long) -> Unit,
    onNavigateToPlayer: (Long, Long) -> Unit,
    viewModel: RecentsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Terkini", style = MaterialTheme.typography.headlineSmall) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = ColorBackground),
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            when (val state = uiState) {
                is RecentsUiState.Loading -> ShimmerEpisodeList()
                is RecentsUiState.Empty -> EmptyState(
                    icon = Icons.Outlined.WatchLater,
                    title = "Belum Ada Aktivitas",
                    description = "Mulai menonton donghua dari tab Jelajah, dan riwayatmu akan muncul di sini.",
                )
                is RecentsUiState.Error -> ErrorState(
                    message = state.message,
                    onRetry = { viewModel.onEvent(RecentsEvent.Retry) },
                )
                is RecentsUiState.Success -> RecentsContent(
                    state = state,
                    viewModel = viewModel,
                    onNavigateToDetail = onNavigateToDetail,
                    onNavigateToPlayer = onNavigateToPlayer,
                )
            }
        }
    }
}

@Composable
private fun RecentsContent(
    state: RecentsUiState.Success,
    viewModel: RecentsViewModel,
    onNavigateToDetail: (Long) -> Unit,
    onNavigateToPlayer: (Long, Long) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp),
    ) {
        // Filter Tabs
        item(key = "tabs") {
            TabRow(
                selectedTabIndex = RecentsTab.entries.indexOf(state.activeTab),
                containerColor = ColorBackground,
                contentColor = ColorPrimary,
            ) {
                RecentsTab.entries.forEach { tab ->
                    Tab(
                        selected = state.activeTab == tab,
                        onClick = { viewModel.onEvent(RecentsEvent.TabChanged(tab)) },
                        text = { Text(tab.label) },
                    )
                }
            }
        }

        // Continue Watching Section
        if (state.activeTab == RecentsTab.ALL || state.activeTab == RecentsTab.CONTINUE) {
            if (state.continueWatching.isNotEmpty()) {
                item(key = "continue_header") {
                    SectionLabel("▶ LANJUTKAN MENONTON")
                }
                items(
                    items = state.continueWatching,
                    key = { "cw_${it.donghua.id}" },
                ) { progress ->
                    val episode = progress.lastEpisode ?: return@items
                    ContinueWatchingCard(
                        donghua = progress.donghua,
                        episode = episode,
                        progress = progress.progress,
                        timestamp = progress.lastWatchedAt,
                        onClick = {
                            onNavigateToPlayer(progress.donghua.id, episode.id)
                        },
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                    )
                }
            }
        }

        // New Episodes Section
        if (state.activeTab == RecentsTab.ALL || state.activeTab == RecentsTab.NEW_EPISODES) {
            if (state.newEpisodes.isNotEmpty()) {
                item(key = "new_header") {
                    SectionLabel("🆕 EPISODE BARU DI LIBRARY")
                }
                items(
                    items = state.newEpisodes,
                    key = { "ne_${it.donghuaId}" },
                ) { update ->
                    NewEpisodeItem(
                        update = update,
                        onClick = { onNavigateToDetail(update.donghuaId) },
                    )
                }
            }
        }

        // History Timeline
        if (state.activeTab == RecentsTab.ALL) {
            state.history.forEach { day ->
                item(key = "day_${day.date}") {
                    SectionLabel("📅 RIWAYAT — ${formatDate(day.date)}")
                }
                items(
                    items = day.items,
                    key = { "h_${it.donghuaId}_${it.watchAt}" },
                ) { item ->
                    HistoryItemRow(
                        item = item,
                        onClick = { onNavigateToDetail(item.donghuaId) },
                    )
                }
            }
        }
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        color = ColorTextSecondary,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
    )
}

@Composable
private fun NewEpisodeItem(
    update: EpisodeUpdate,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            model = update.coverUrl,
            contentDescription = null,
            modifier = Modifier
                .size(48.dp, 64.dp)
                .clip(MaterialTheme.shapes.small),
            contentScale = ContentScale.Crop,
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = update.donghuaTitle,
                style = MaterialTheme.typography.titleSmall,
                color = ColorTextPrimary,
                maxLines = 1,
            )
            val epText = update.episodeNumbers.take(3).joinToString(", ") { "Ep ${it.toInt()}" } +
                if (update.episodeNumbers.size > 3) " +${update.episodeNumbers.size - 3}" else ""
            Text(
                text = "$epText baru",
                style = MaterialTheme.typography.bodySmall,
                color = ColorSecondary,
            )
            Text(
                text = formatTimestamp(update.timestamp),
                style = MaterialTheme.typography.labelSmall,
                color = ColorTextTertiary,
            )
        }
    }
}

@Composable
private fun HistoryItemRow(
    item: HistoryItem,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            model = item.coverUrl,
            contentDescription = null,
            modifier = Modifier
                .size(48.dp, 64.dp)
                .clip(MaterialTheme.shapes.small),
            contentScale = ContentScale.Crop,
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.donghuaTitle,
                style = MaterialTheme.typography.titleSmall,
                color = ColorTextPrimary,
                maxLines = 1,
            )
            Text(
                text = "Ep ${item.episodeNumber.toInt()}${item.episodeTitle?.let { " - $it" } ?: ""}",
                style = MaterialTheme.typography.bodySmall,
                color = ColorTextSecondary,
            )
            LinearProgressIndicator(
                progress = { item.progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .padding(top = 4.dp),
                color = ColorSecondary,
                trackColor = ColorSurfaceVariant,
            )
        }

        Text(
            text = formatTime(item.watchAt),
            style = MaterialTheme.typography.labelSmall,
            color = ColorTextTertiary,
            modifier = Modifier.padding(start = 8.dp),
        )
    }
}

private fun formatDate(date: java.time.LocalDate): String {
    val today = java.time.LocalDate.now()
    return when (date) {
        today -> "HARI INI"
        today.minusDays(1) -> "KEMARIN"
        else -> date.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
    }
}

private fun formatTimestamp(timestamp: Long): String {
    val diff = System.currentTimeMillis() - timestamp
    val hours = TimeUnit.MILLISECONDS.toHours(diff)
    return when {
        hours < 1 -> "${TimeUnit.MILLISECONDS.toMinutes(diff)} menit lalu"
        hours < 24 -> "$hours jam lalu"
        hours < 48 -> "kemarin"
        else -> "${hours / 24} hari lalu"
    }
}

private fun formatTime(timestamp: Long): String {
    val instant = java.time.Instant.ofEpochMilli(timestamp)
    val time = instant.atZone(java.time.ZoneId.systemDefault()).toLocalTime()
    return time.format(DateTimeFormatter.ofPattern("HH:mm"))
}
