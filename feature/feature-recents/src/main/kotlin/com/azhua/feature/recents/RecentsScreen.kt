package com.azhua.feature.recents

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.azhua.core.ui.theme.ColorBackground

@Composable
fun RecentsScreen(
    onNavigateToDetail: (Long) -> Unit,
    onNavigateToPlayer: (Long, Long) -> Unit,
    viewModel: RecentsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Scaffold(
        topBar = { TopAppBar(title = { Text("Terkini") }, colors = TopAppBarDefaults.topAppBarColors(containerColor = ColorBackground)) }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) { Text("Recents screen") }
    }
}
