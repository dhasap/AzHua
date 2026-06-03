package com.azhua.feature.extensions

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Extension
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.azhua.core.model.ExtensionStatus
import com.azhua.core.ui.component.*
import com.azhua.core.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExtensionScreen(
    onNavigateToSettings: (String) -> Unit,
    viewModel: ExtensionViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Paviliun", style = MaterialTheme.typography.headlineSmall) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = ColorBackground),
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            when (val state = uiState) {
                is ExtensionUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = ColorPrimary)
                    }
                }
                is ExtensionUiState.Empty -> EmptyState(
                    icon = Icons.Outlined.Extension,
                    title = "Belum Ada Ekstensi",
                    description = "Ekstensi sumber donghua akan muncul di sini.",
                )
                is ExtensionUiState.Error -> ErrorState(
                    message = state.message,
                    onRetry = { viewModel.onEvent(ExtensionEvent.Retry) },
                )
                is ExtensionUiState.Success -> ExtensionContent(
                    state = state,
                    viewModel = viewModel,
                    onNavigateToSettings = onNavigateToSettings,
                )
            }
        }
    }
}

@Composable
private fun ExtensionContent(
    state: ExtensionUiState.Success,
    viewModel: ExtensionViewModel,
    onNavigateToSettings: (String) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Tabs
        TabRow(
            selectedTabIndex = ExtensionTab.entries.indexOf(state.activeTab),
            containerColor = ColorBackground,
            contentColor = ColorPrimary,
        ) {
            ExtensionTab.entries.forEach { tab ->
                val count = when (tab) {
                    ExtensionTab.INSTALLED -> state.installed.size
                    ExtensionTab.UPDATE -> state.updatable.size
                    ExtensionTab.AVAILABLE -> state.available.size
                }
                Tab(
                    selected = state.activeTab == tab,
                    onClick = { viewModel.onEvent(ExtensionEvent.TabChanged(tab)) },
                    text = {
                        Text(
                            text = if (count > 0) "${tab.label} ($count)" else tab.label,
                        )
                    },
                )
            }
        }

        // Content
        val items = when (state.activeTab) {
            ExtensionTab.INSTALLED -> state.installed
            ExtensionTab.UPDATE -> state.updatable
            ExtensionTab.AVAILABLE -> state.available
        }

        if (items.isEmpty()) {
            EmptyState(
                icon = Icons.Outlined.Extension,
                title = when (state.activeTab) {
                    ExtensionTab.INSTALLED -> "Belum Ada Ekstensi Terinstal"
                    ExtensionTab.UPDATE -> "Semua Ekstensi Sudah Terbaru"
                    ExtensionTab.AVAILABLE -> "Tidak Ada Ekstensi Tersedia"
                },
                description = "Ekstensi baru akan muncul di sini.",
                modifier = Modifier.padding(top = 48.dp),
            )
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(items, key = { it.extension.id }) { item ->
                    ExtensionCard(
                        item = item,
                        onInstall = { viewModel.onEvent(ExtensionEvent.Install(item.extension.id)) },
                        onUninstall = { viewModel.onEvent(ExtensionEvent.Uninstall(item.extension.id)) },
                        onUpdate = { viewModel.onEvent(ExtensionEvent.Update(item.extension.id)) },
                        onSettings = { onNavigateToSettings(item.extension.packageName) },
                    )
                }
            }
        }
    }
}

@Composable
private fun ExtensionCard(
    item: ExtensionItem,
    onInstall: () -> Unit,
    onUninstall: () -> Unit,
    onUpdate: () -> Unit,
    onSettings: () -> Unit,
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = ColorSurface),
        shape = MaterialTheme.shapes.medium,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Icon placeholder
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = ColorSurfaceVariant,
                modifier = Modifier.size(48.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = item.extension.name.take(2).uppercase(),
                        style = MaterialTheme.typography.titleMedium,
                        color = ColorPrimary,
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.extension.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = ColorTextPrimary,
                )
                Text(
                    text = "${item.extension.lang.uppercase()} · v${item.extension.versionName}",
                    style = MaterialTheme.typography.bodySmall,
                    color = ColorTextSecondary,
                )
            }

            // Action button
            when (item.status) {
                ExtensionStatus.AVAILABLE -> {
                    AzButton(
                        text = "Install",
                        onClick = onInstall,
                        variant = ButtonVariant.Primary,
                        isLoading = item.isLoading,
                    )
                }
                ExtensionStatus.INSTALLED -> {
                    Row {
                        AzButton(
                            text = "Pengaturan",
                            onClick = onSettings,
                            variant = ButtonVariant.Ghost,
                        )
                    }
                }
                ExtensionStatus.UPDATE_AVAILABLE -> {
                    AzButton(
                        text = "Perbarui",
                        onClick = onUpdate,
                        variant = ButtonVariant.Secondary,
                        isLoading = item.isLoading,
                    )
                }
                ExtensionStatus.INCOMPATIBLE -> {
                    AzButton(
                        text = "Tidak Kompatibel",
                        onClick = {},
                        variant = ButtonVariant.Danger,
                        enabled = false,
                    )
                }
            }
        }
    }
}
