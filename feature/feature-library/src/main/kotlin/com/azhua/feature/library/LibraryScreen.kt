package com.azhua.feature.library

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocalLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.azhua.core.ui.component.EmptyState
import com.azhua.core.ui.component.ErrorState
import com.azhua.core.ui.component.ShimmerDonghuaGrid
import com.azhua.core.ui.theme.ColorBackground

@Composable
fun LibraryScreen(
    onNavigateToDetail: (Long) -> Unit,
    viewModel: LibraryViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pusaka") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = ColorBackground),
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (val state = uiState) {
                is LibraryUiState.Loading -> ShimmerDonghuaGrid()
                is LibraryUiState.Empty -> EmptyState(
                    icon = Icons.Outlined.LocalLibrary,
                    title = "Pusaka Masih Kosong",
                    description = "Mulai tambahkan donghua favoritmu!",
                    actionLabel = "Pergi ke Jelajah",
                )
                is LibraryUiState.Error -> ErrorState(
                    message = state.message,
                    onRetry = { viewModel.retry() },
                )
                is LibraryUiState.Success -> {
                    Text("Library loaded: ${state.donghuaCount} items")
                }
            }
        }
    }
}
