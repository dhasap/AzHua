package com.azhua.feature.discover

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.azhua.core.ui.theme.ColorBackground

@Composable
fun DiscoverScreen(
    onNavigateToDetail: (Long) -> Unit,
    onNavigateToSource: (String) -> Unit,
    viewModel: DiscoverViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Scaffold(
        topBar = { TopAppBar(title = { Text("Jelajah") }, colors = TopAppBarDefaults.topAppBarColors(containerColor = ColorBackground)) }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) { Text("Discover screen") }
    }
}
