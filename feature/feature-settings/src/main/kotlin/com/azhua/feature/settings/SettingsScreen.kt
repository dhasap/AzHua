package com.azhua.feature.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.azhua.core.ui.theme.ColorBackground

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onNavigateTo: (String) -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Scaffold(
        topBar = { TopAppBar(title = { Text("Pengaturan") }, navigationIcon = { IconButton(onClick = onBack) {} }, colors = TopAppBarDefaults.topAppBarColors(containerColor = ColorBackground)) }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) { Text("Settings screen") }
    }
}
