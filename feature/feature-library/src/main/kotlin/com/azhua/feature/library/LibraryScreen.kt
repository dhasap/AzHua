package com.azhua.feature.library

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material.icons.outlined.LocalLibrary
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.azhua.core.ui.component.*
import com.azhua.core.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun LibraryScreen(
    onNavigateToDetail: (Long) -> Unit,
    viewModel: LibraryViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showSortMenu by remember { mutableStateOf(false) }
    var showFilterMenu by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            val state = uiState
            if (state is LibraryUiState.Success && state.isMultiSelectMode) {
                // Multi-select top bar
                TopAppBar(
                    title = { Text("${state.selectedDonghuaIds.size} dipilih") },
                    navigationIcon = {
                        IconButton(onClick = { viewModel.onEvent(LibraryEvent.ClearSelection) }) {
                            Icon(Icons.Filled.Close, "Batal")
                        }
                    },
                    actions = {
                        IconButton(onClick = { viewModel.onEvent(LibraryEvent.SelectAll) }) {
                            Icon(Icons.Filled.SelectAll, "Pilih Semua")
                        }
                        IconButton(onClick = {
                            viewModel.onEvent(LibraryEvent.RemoveFromLibrary(state.selectedDonghuaIds))
                        }) {
                            // Delete icon
                            Text("🗑️")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = ColorSurface),
                )
            } else {
                TopAppBar(
                    title = {
                        Text(
                            "Pusaka",
                            style = MaterialTheme.typography.headlineSmall,
                        )
                    },
                    actions = {
                        IconButton(onClick = { viewModel.onEvent(LibraryEvent.ToggleSearch) }) {
                            Icon(Icons.Filled.Search, "Cari")
                        }
                        Box {
                            IconButton(onClick = { showSortMenu = true }) {
                                Icon(Icons.Outlined.FilterList, "Filter")
                            }
                            DropdownMenu(
                                expanded = showSortMenu,
                                onDismissRequest = { showSortMenu = false },
                            ) {
                                Text(
                                    "Urutkan",
                                    style = MaterialTheme.typography.labelLarge,
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                )
                                SortOption.entries.forEach { option ->
                                    DropdownMenuItem(
                                        text = { Text(option.label) },
                                        onClick = {
                                            viewModel.onEvent(LibraryEvent.SortChanged(option))
                                            showSortMenu = false
                                        },
                                    )
                                }
                                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                                Text(
                                    "Filter Status",
                                    style = MaterialTheme.typography.labelLarge,
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                )
                                StatusFilter.entries.forEach { filter ->
                                    DropdownMenuItem(
                                        text = { Text(filter.label) },
                                        onClick = {
                                            viewModel.onEvent(LibraryEvent.StatusFilterChanged(filter))
                                            showSortMenu = false
                                        },
                                    )
                                }
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = ColorBackground),
                )
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            when (val state = uiState) {
                is LibraryUiState.Loading -> ShimmerDonghuaGrid()
                is LibraryUiState.Empty -> EmptyState(
                    icon = Icons.Outlined.LocalLibrary,
                    title = "Pusaka Masih Kosong",
                    description = "Mulai tambahkan donghua favoritmu! Jelajahi sumber di tab Jelajah.",
                    actionLabel = "Pergi ke Jelajah",
                )
                is LibraryUiState.Error -> ErrorState(
                    message = state.message,
                    onRetry = { viewModel.onEvent(LibraryEvent.Retry) },
                )
                is LibraryUiState.Success -> LibraryContent(
                    state = state,
                    viewModel = viewModel,
                    onNavigateToDetail = onNavigateToDetail,
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun LibraryContent(
    state: LibraryUiState.Success,
    viewModel: LibraryViewModel,
    onNavigateToDetail: (Long) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp),
    ) {
        // Continue Watching Section
        if (state.continueWatching.isNotEmpty()) {
            item(key = "continue_header") {
                Text(
                    text = "Lanjutkan Menonton",
                    style = MaterialTheme.typography.labelLarge,
                    color = ColorTextSecondary,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                )
            }
            item(key = "continue_row") {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(
                        items = state.continueWatching,
                        key = { it.donghua.id },
                    ) { progress ->
                        ContinueWatchingCard(
                            donghua = progress.donghua,
                            episode = progress.lastEpisode ?: return@items,
                            progress = progress.progress,
                            timestamp = progress.lastWatchedAt,
                            onClick = { onNavigateToDetail(progress.donghua.id) },
                            modifier = Modifier.width(280.dp),
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        // Search Bar (when active)
        if (state.isSearchActive) {
            item(key = "search_bar") {
                OutlinedTextField(
                    value = state.searchQuery,
                    onValueChange = { viewModel.onEvent(LibraryEvent.SearchQueryChanged(it)) },
                    placeholder = { Text("Cari di Pusaka...", color = ColorTextTertiary) },
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
                    leadingIcon = {
                        Icon(Icons.Filled.Search, null, tint = ColorIconDefault)
                    },
                    trailingIcon = {
                        if (state.searchQuery.isNotEmpty()) {
                            IconButton(onClick = {
                                viewModel.onEvent(LibraryEvent.SearchQueryChanged(""))
                            }) {
                                Icon(Icons.Filled.Close, "Hapus", tint = ColorIconDefault)
                            }
                        }
                    },
                )
            }
        }

        // Filter Chips
        item(key = "filter_chips") {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(vertical = 4.dp),
            ) {
                items(StatusFilter.entries) { filter ->
                    AzChip(
                        text = filter.label,
                        selected = state.activeFilter.statusFilter == filter,
                        onClick = { viewModel.onEvent(LibraryEvent.StatusFilterChanged(filter)) },
                    )
                }
            }
        }

        // Category Sections
        state.categories.forEach { categoryWithDonghua ->
            val category = categoryWithDonghua.category
            val isExpanded = category.id in state.expandedCategoryIds

            stickyHeader(key = "header_${category.id}") {
                CategoryHeader(
                    category = category,
                    isExpanded = isExpanded,
                    itemCount = categoryWithDonghua.donghuaList.size,
                    onToggle = { viewModel.onEvent(LibraryEvent.ToggleCategory(category.id)) },
                )
            }

            if (isExpanded && categoryWithDonghua.donghuaList.isNotEmpty()) {
                item(key = "grid_${category.id}") {
                    // Staggered Grid
                    val columns = StaggeredGridCells.Fixed(state.gridColumns)
                    LazyVerticalStaggeredGrid(
                        columns = columns,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 2000.dp) // Prevent nested scroll issues
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalItemSpacing = 8.dp,
                    ) {
                        items(
                            items = categoryWithDonghua.donghuaList,
                            key = { it.id },
                        ) { donghua ->
                            val isSelected = donghua.id in state.selectedDonghuaIds
                            DonghuaCard(
                                donghua = donghua,
                                onClick = {
                                    if (state.isMultiSelectMode) {
                                        viewModel.onEvent(LibraryEvent.DonghuaSelected(donghua.id))
                                    } else {
                                        onNavigateToDetail(donghua.id)
                                    }
                                },
                                onLongClick = {
                                    viewModel.enableMultiSelect(donghua.id)
                                },
                                modifier = Modifier.then(
                                    if (isSelected) Modifier.padding(2.dp) else Modifier
                                ),
                            )
                        }
                    }
                }
            }
        }
    }
}
