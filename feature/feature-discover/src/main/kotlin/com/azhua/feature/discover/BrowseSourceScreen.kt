package com.azhua.feature.discover

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.azhua.core.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrowseSourceScreen(
    sourceId: String,
    onBack: () -> Unit,
    onNavigateToDetail: (Long) -> Unit,
    viewModel: DiscoverViewModel = hiltViewModel(),
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(sourceId) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = ColorBackground),
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            // TODO: Implement browse source with 3-column grid + infinite scroll
            Text("Browse source: $sourceId")
        }
    }
}
