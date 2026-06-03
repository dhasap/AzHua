package com.azhua.feature.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.azhua.core.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onNavigateTo: (String) -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pengaturan", style = MaterialTheme.typography.headlineSmall) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = ColorBackground),
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentPadding = PaddingValues(bottom = 32.dp),
        ) {
            // Profile Section
            item(key = "profile") {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(containerColor = ColorSurface),
                    shape = MaterialTheme.shapes.large,
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Surface(
                            shape = MaterialTheme.shapes.extraLarge,
                            color = ColorPrimaryContainer,
                            modifier = Modifier.size(56.dp),
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "🐉",
                                    style = MaterialTheme.typography.headlineMedium,
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = "Profil AzHua",
                                style = MaterialTheme.typography.titleLarge,
                                color = ColorTextPrimary,
                            )
                            Text(
                                text = "${uiState.totalDonghua} Donghua · ${uiState.totalEpisodes} Episode",
                                style = MaterialTheme.typography.bodySmall,
                                color = ColorTextSecondary,
                            )
                        }
                    }
                }
            }

            // Appearance Section
            item(key = "appearance_header") {
                SectionHeader(icon = "🎨", title = "TAMPILAN")
            }
            item(key = "theme") {
                SettingsDropdownItem(
                    icon = Icons.Outlined.Palette,
                    title = "Tema",
                    value = uiState.theme.label,
                    options = AppTheme.entries.map { it.label },
                    onSelected = { idx ->
                        viewModel.onEvent(SettingsEvent.ThemeChanged(AppTheme.entries[idx]))
                    },
                )
            }
            item(key = "grid") {
                SettingsDropdownItem(
                    icon = Icons.Outlined.GridView,
                    title = "Ukuran Grid",
                    value = "${uiState.gridColumns} kolom",
                    options = listOf("2 kolom", "3 kolom"),
                    onSelected = { idx ->
                        viewModel.onEvent(SettingsEvent.GridColumnsChanged(idx + 2))
                    },
                )
            }
            item(key = "text_size") {
                SettingsSliderItem(
                    icon = Icons.Outlined.TextFields,
                    title = "Ukuran Teks",
                    value = uiState.textSize,
                    valueRange = 0.8f..1.2f,
                    onValueChange = { viewModel.onEvent(SettingsEvent.TextSizeChanged(it)) },
                )
            }

            // Player Section
            item(key = "player_header") {
                SectionHeader(icon = "▶", title = "PEMUTAR")
            }
            item(key = "quality") {
                SettingsDropdownItem(
                    icon = Icons.Outlined.HighQuality,
                    title = "Kualitas Default",
                    value = uiState.defaultQuality,
                    options = listOf("1080p", "720p", "480p", "360p"),
                    onSelected = { idx ->
                        viewModel.onEvent(SettingsEvent.DefaultQualityChanged(listOf("1080p", "720p", "480p", "360p")[idx]))
                    },
                )
            }
            item(key = "autoplay") {
                SettingsSwitchItem(
                    icon = Icons.Outlined.SkipNext,
                    title = "Auto-play Berikutnya",
                    checked = uiState.autoPlayNext,
                    onCheckedChange = { viewModel.onEvent(SettingsEvent.AutoPlayNextChanged(it)) },
                )
            }
            item(key = "skip") {
                SettingsDropdownItem(
                    icon = Icons.Outlined.FastForward,
                    title = "Durasi Mundur",
                    value = "${uiState.skipIntroDuration} detik",
                    options = listOf("5 detik", "10 detik", "15 detik", "30 detik"),
                    onSelected = { idx ->
                        viewModel.onEvent(SettingsEvent.SkipIntroDurationChanged(listOf(5, 10, 15, 30)[idx]))
                    },
                )
            }

            // Library Section
            item(key = "library_header") {
                SectionHeader(icon = "📚", title = "LIBRARY")
            }
            item(key = "update_on_open") {
                SettingsSwitchItem(
                    icon = Icons.Outlined.Refresh,
                    title = "Perbarui saat Buka",
                    checked = uiState.updateOnOpen,
                    onCheckedChange = { viewModel.onEvent(SettingsEvent.UpdateOnOpenChanged(it)) },
                )
            }
            item(key = "notify") {
                SettingsSwitchItem(
                    icon = Icons.Outlined.Notifications,
                    title = "Notif Episode Baru",
                    checked = uiState.notifyNewEpisodes,
                    onCheckedChange = { viewModel.onEvent(SettingsEvent.NotifyNewEpisodesChanged(it)) },
                )
            }

            // Data Section
            item(key = "data_header") {
                SectionHeader(icon = "💾", title = "DATA & PENYIMPANAN")
            }
            item(key = "backup") {
                SettingsClickableItem(
                    icon = Icons.Outlined.Backup,
                    title = "Buat Cadangan",
                    onClick = { viewModel.onEvent(SettingsEvent.CreateBackup) },
                )
            }
            item(key = "restore") {
                SettingsClickableItem(
                    icon = Icons.Outlined.Restore,
                    title = "Pulihkan Cadangan",
                    onClick = { viewModel.onEvent(SettingsEvent.RestoreBackup) },
                )
            }
            item(key = "cache") {
                SettingsClickableItem(
                    icon = Icons.Outlined.DeleteSweep,
                    title = "Hapus Cache",
                    subtitle = "Menghitung...",
                    onClick = { viewModel.onEvent(SettingsEvent.ClearCache) },
                )
            }

            // About
            item(key = "about") {
                SettingsClickableItem(
                    icon = Icons.Outlined.Info,
                    title = "Tentang",
                    subtitle = "AzHua v2.0.0",
                    onClick = {},
                )
            }
        }
    }
}

@Composable
private fun SectionHeader(icon: String, title: String) {
    Text(
        text = "$icon  $title",
        style = MaterialTheme.typography.labelLarge,
        color = ColorTextSecondary,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
    )
}

@Composable
private fun SettingsSwitchItem(
    icon: ImageVector,
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    ListItem(
        headlineContent = { Text(title, color = ColorTextPrimary) },
        leadingContent = { Icon(icon, null, tint = ColorIconDefault) },
        trailingContent = {
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = ColorPrimary,
                    checkedTrackColor = ColorPrimaryContainer,
                ),
            )
        },
        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
        modifier = Modifier.clickable { onCheckedChange(!checked) },
    )
}

@Composable
private fun SettingsDropdownItem(
    icon: ImageVector,
    title: String,
    value: String,
    options: List<String>,
    onSelected: (Int) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    ListItem(
        headlineContent = { Text(title, color = ColorTextPrimary) },
        supportingContent = { Text(value, color = ColorTextSecondary) },
        leadingContent = { Icon(icon, null, tint = ColorIconDefault) },
        trailingContent = {
            Box {
                Text("▼", color = ColorTextTertiary)
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    options.forEachIndexed { idx, option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                onSelected(idx)
                                expanded = false
                            },
                        )
                    }
                }
            }
        },
        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
        modifier = Modifier.clickable { expanded = true },
    )
}

@Composable
private fun SettingsSliderItem(
    icon: ImageVector,
    title: String,
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    onValueChange: (Float) -> Unit,
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = ColorIconDefault)
            Spacer(modifier = Modifier.width(16.dp))
            Text(title, color = ColorTextPrimary, modifier = Modifier.weight(1f))
            Text(
                text = String.format("%.1fx", value),
                style = MaterialTheme.typography.bodySmall,
                color = ColorTextSecondary,
            )
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
            colors = SliderDefaults.colors(
                thumbColor = ColorPrimary,
                activeTrackColor = ColorPrimary,
            ),
        )
    }
}

@Composable
private fun SettingsClickableItem(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    onClick: () -> Unit,
) {
    ListItem(
        headlineContent = { Text(title, color = ColorTextPrimary) },
        supportingContent = subtitle?.let { { Text(it, color = ColorTextSecondary) } },
        leadingContent = { Icon(icon, null, tint = ColorIconDefault) },
        trailingContent = { Text("→", color = ColorTextTertiary) },
        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
        modifier = Modifier.clickable(onClick = onClick),
    )
}
