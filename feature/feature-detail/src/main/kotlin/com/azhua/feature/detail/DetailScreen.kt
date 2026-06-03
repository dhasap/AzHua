package com.azhua.feature.detail

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.azhua.core.ui.component.*
import com.azhua.core.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    donghuaId: Long,
    onBack: () -> Unit,
    onPlayEpisode: (Long) -> Unit,
    viewModel: DetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when {
        uiState.isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = ColorPrimary)
            }
        }
        uiState.error != null -> {
            ErrorState(
                message = uiState.error!!,
                onRetry = { viewModel.onEvent(DetailEvent.Retry) },
            )
        }
        uiState.donghua != null -> {
            DetailContent(
                state = uiState,
                onBack = onBack,
                onPlayEpisode = onPlayEpisode,
                viewModel = viewModel,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetailContent(
    state: DetailUiState,
    onBack: () -> Unit,
    onPlayEpisode: (Long) -> Unit,
    viewModel: DetailViewModel,
) {
    val donghua = state.donghua!!

    Scaffold(
        topBar = {
            TopAppBar(
                title = { /* Title appears on scroll */ },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Kembali", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.onEvent(DetailEvent.ToggleLibrary) }) {
                        Icon(
                            imageVector = if (state.isInLibrary) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
                            contentDescription = if (state.isInLibrary) "Hapus dari Pusaka" else "Tambah ke Pusaka",
                            tint = if (state.isInLibrary) ColorPrimary else Color.White,
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                ),
            )
        }
    ) { paddingValues ->
        // Compute filtered episodes outside LazyColumn
        val filteredEpisodes = remember(state.episodes, state.episodeFilter, state.episodeSortOrder, state.searchQuery) {
            state.episodes.let { eps ->
                val filtered = when (state.episodeFilter) {
                    EpisodeFilter.ALL -> eps
                    EpisodeFilter.UNWATCHED -> eps.filter { !it.isWatched }
                    EpisodeFilter.WATCHED -> eps.filter { it.isWatched }
                    EpisodeFilter.DOWNLOADED -> eps.filter { it.isDownloaded }
                }
                val searched = if (state.searchQuery.isBlank()) filtered
                else filtered.filter { (it.title ?: "").contains(state.searchQuery, ignoreCase = true) }
                when (state.episodeSortOrder) {
                    EpisodeSortOrder.NEWEST -> searched.sortedByDescending { it.episodeNumber }
                    EpisodeSortOrder.OLDEST -> searched.sortedBy { it.episodeNumber }
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(ColorBackground),
        ) {
            // Cover Header
            item(key = "header") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                ) {
                    AsyncImage(
                        model = donghua.coverUrl,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                    )
                    // Gradient overlay
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Transparent,
                                        ColorBackground.copy(alpha = 0.7f),
                                        ColorBackground,
                                    )
                                )
                            )
                    )
                }
            }

            // Title & Info
            item(key = "info") {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(
                        text = donghua.title,
                        style = MaterialTheme.typography.headlineLarge,
                        color = ColorTextPrimary,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    val subtitle = listOfNotNull(
                        donghua.studio,
                        donghua.year?.toString(),
                        donghua.status.name,
                    ).joinToString(" · ")
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = ColorTextSecondary,
                    )
                    if (donghua.rating > 0) {
                        Text(
                            text = "⭐ ${donghua.rating}",
                            style = MaterialTheme.typography.bodySmall,
                            color = ColorPrimary,
                        )
                    }
                }
            }

            // CTA Buttons
            item(key = "cta") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    if (state.isInLibrary) {
                        AzButton(
                            text = "✓ Ada di Pusaka",
                            onClick = { viewModel.onEvent(DetailEvent.ToggleLibrary) },
                            variant = ButtonVariant.Outlined,
                            modifier = Modifier.weight(1f),
                        )
                    } else {
                        AzButton(
                            text = "+ Tambah ke Pusaka",
                            onClick = { viewModel.onEvent(DetailEvent.ToggleLibrary) },
                            variant = ButtonVariant.Primary,
                            modifier = Modifier.weight(1f),
                        )
                    }
                    AzButton(
                        text = "▶ Mulai Tonton",
                        onClick = {
                            state.episodes.firstOrNull()?.let { onPlayEpisode(it.id) }
                        },
                        variant = ButtonVariant.Secondary,
                        modifier = Modifier.weight(1f),
                    )
                }
            }

            // Genre Chips
            if (donghua.genres.isNotEmpty()) {
                item(key = "genres") {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(donghua.genres) { genre ->
                            AzChip(text = genre, onClick = {})
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            // Synopsis
            val synopsis = donghua.synopsis
            if (!synopsis.isNullOrBlank()) {
                item(key = "synopsis") {
                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        Text(
                            text = "Sinopsis",
                            style = MaterialTheme.typography.headlineSmall,
                            color = ColorTextPrimary,
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        var expanded by remember { mutableStateOf(false) }
                        Text(
                            text = synopsis,
                            style = MaterialTheme.typography.bodyMedium,
                            color = ColorTextSecondary,
                            maxLines = if (expanded) Int.MAX_VALUE else 3,
                            overflow = TextOverflow.Ellipsis,
                        )
                        if (synopsis.length > 150) {
                            TextButton(onClick = { expanded = !expanded }) {
                                Text(
                                    text = if (expanded) "Lebih Sedikit ▲" else "Selengkapnya ▼",
                                    color = ColorPrimary,
                                    style = MaterialTheme.typography.labelMedium,
                                )
                            }
                        }
                    }
                }
            }

            // Episode List Header
            item(key = "episode_header") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Episode (${state.episodes.size})",
                        style = MaterialTheme.typography.headlineSmall,
                        color = ColorTextPrimary,
                        modifier = Modifier.weight(1f),
                    )
                    // Sort dropdown
                    var showSort by remember { mutableStateOf(false) }
                    Box {
                        TextButton(onClick = { showSort = true }) {
                            Text(
                                text = "Urutkan: ${state.episodeSortOrder.label} ▼",
                                style = MaterialTheme.typography.labelMedium,
                                color = ColorTextSecondary,
                            )
                        }
                        DropdownMenu(expanded = showSort, onDismissRequest = { showSort = false }) {
                            EpisodeSortOrder.entries.forEach { sort ->
                                DropdownMenuItem(
                                    text = { Text(sort.label) },
                                    onClick = {
                                        viewModel.onEvent(DetailEvent.SortChanged(sort))
                                        showSort = false
                                    },
                                )
                            }
                        }
                    }
                }
            }

            // Episode Filter
            item(key = "episode_filter") {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 8.dp),
                ) {
                    items(EpisodeFilter.entries) { filter ->
                        AzChip(
                            text = filter.label,
                            selected = state.episodeFilter == filter,
                            onClick = { viewModel.onEvent(DetailEvent.FilterChanged(filter)) },
                        )
                    }
                }
            }

            items(
                items = filteredEpisodes,
                key = { it.id },
            ) { episode ->
                com.azhua.core.ui.component.EpisodeItem(
                    episode = episode,
                    isWatched = episode.isWatched,
                    isDownloaded = episode.isDownloaded,
                    watchProgress = episode.watchProgress,
                    onClick = { onPlayEpisode(episode.id) },
                )
            }

            // Bottom spacing
            item(key = "bottom_spacer") {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}
