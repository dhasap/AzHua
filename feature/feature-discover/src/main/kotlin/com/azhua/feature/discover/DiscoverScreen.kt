package com.azhua.feature.discover

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.azhua.core.ui.component.*
import com.azhua.core.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscoverScreen(
    onNavigateToDetail: (Long) -> Unit,
    onNavigateToSource: (String) -> Unit,
    viewModel: DiscoverViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Jelajah", style = MaterialTheme.typography.headlineSmall)
                },
                actions = {
                    IconButton(onClick = { viewModel.onEvent(DiscoverEvent.ToggleSearch) }) {
                        Icon(Icons.Filled.Search, "Cari")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = ColorBackground),
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            when (val state = uiState) {
                is DiscoverUiState.Loading -> ShimmerDonghuaGrid(columns = 3)
                is DiscoverUiState.Empty -> EmptyState(
                    icon = Icons.Outlined.Explore,
                    title = "Belum Ada Sumber",
                    description = "Instal ekstensi dari tab Paviliun untuk mulai menjelajahi donghua.",
                )
                is DiscoverUiState.Error -> ErrorState(
                    message = state.message,
                    onRetry = { viewModel.onEvent(DiscoverEvent.Retry) },
                )
                is DiscoverUiState.Success -> DiscoverContent(
                    state = state,
                    viewModel = viewModel,
                    onNavigateToDetail = onNavigateToDetail,
                    onNavigateToSource = onNavigateToSource,
                )
            }
        }
    }
}

@Composable
private fun DiscoverContent(
    state: DiscoverUiState.Success,
    viewModel: DiscoverViewModel,
    onNavigateToDetail: (Long) -> Unit,
    onNavigateToSource: (String) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp),
    ) {
        // Search Bar (when active)
        if (state.isSearchActive) {
            item(key = "search") {
                OutlinedTextField(
                    value = state.searchQuery,
                    onValueChange = { viewModel.onEvent(DiscoverEvent.SearchQueryChanged(it)) },
                    placeholder = { Text("Cari donghua...", color = ColorTextTertiary) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    shape = MaterialTheme.shapes.extraLarge,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ColorPrimary,
                        unfocusedBorderColor = ColorOutlineVariant,
                        focusedContainerColor = ColorSurfaceContainerHigh,
                        unfocusedContainerColor = ColorSurfaceContainerHigh,
                    ),
                    singleLine = true,
                    leadingIcon = { Icon(Icons.Filled.Search, null, tint = ColorIconDefault) },
                    trailingIcon = {
                        IconButton(onClick = { viewModel.onEvent(DiscoverEvent.ToggleSearch) }) {
                            Icon(Icons.Filled.Close, "Tutup", tint = ColorIconDefault)
                        }
                    },
                )
            }
        }

        // Source Tabs
        item(key = "source_tabs") {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(vertical = 8.dp),
            ) {
                item {
                    AzChip(
                        text = "Semua",
                        selected = state.selectedSource == null,
                        onClick = { viewModel.onEvent(DiscoverEvent.SelectSource(null)) },
                    )
                }
                items(state.sources) { source ->
                    AzChip(
                        text = source.name,
                        selected = state.selectedSource?.id == source.id,
                        onClick = { viewModel.onEvent(DiscoverEvent.SelectSource(source)) },
                    )
                }
            }
        }

        // Trending Section
        if (state.trending.isNotEmpty()) {
            item(key = "trending_header") {
                SectionHeader(icon = "🔥", title = "Trending Minggu Ini")
            }
            item(key = "trending_row") {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(state.trending, key = { it.id }) { donghua ->
                        DonghuaCard(
                            donghua = donghua,
                            variant = DonghuaCardVariant.Featured,
                            onClick = { onNavigateToDetail(donghua.id) },
                            modifier = Modifier.width(200.dp),
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        // Popular Section
        if (state.popular.isNotEmpty()) {
            item(key = "popular_header") {
                SectionHeader(icon = "⭐", title = "Populer Bulan Ini")
            }
            item(key = "popular_row") {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(state.popular, key = { it.id }) { donghua ->
                        DonghuaCard(
                            donghua = donghua,
                            onClick = { onNavigateToDetail(donghua.id) },
                            modifier = Modifier.width(140.dp),
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        // Latest Updates Section
        if (state.latest.isNotEmpty()) {
            item(key = "latest_header") {
                SectionHeader(icon = "🆕", title = "Update Terbaru")
            }
            items(state.latest, key = { it.id }) { donghua ->
                // Compact card for latest updates
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    DonghuaCard(
                        donghua = donghua,
                        variant = DonghuaCardVariant.Compact,
                        onClick = { onNavigateToDetail(donghua.id) },
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }

        // Empty content message
        if (state.trending.isEmpty() && state.popular.isEmpty() && state.latest.isEmpty()) {
            item(key = "empty_content") {
                EmptyState(
                    icon = Icons.Outlined.Explore,
                    title = "Belum Ada Konten",
                    description = "Instal ekstensi dan muat ulang untuk melihat konten.",
                    modifier = Modifier.padding(top = 48.dp),
                )
            }
        }
    }
}

@Composable
private fun SectionHeader(icon: String, title: String) {
    Text(
        text = "$icon $title",
        style = MaterialTheme.typography.headlineSmall,
        color = ColorTextPrimary,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
    )
}
