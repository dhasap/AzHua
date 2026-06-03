package com.azhua.feature.player

import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.azhua.core.ui.theme.*

@Composable
fun PlayerScreen(
    donghuaId: Long,
    episodeId: Long,
    onBack: () -> Unit,
    viewModel: PlayerViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val view = LocalView.current

    DisposableEffect(Unit) {
        val activity = view.context as? Activity
        val originalOrientation = activity?.requestedOrientation

        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
        val window = activity?.window
        if (window != null) {
            WindowCompat.setDecorFitsSystemWindows(window, false)
            WindowInsetsControllerCompat(window, view).apply {
                hide(androidx.core.view.WindowInsetsCompat.Type.systemBars())
                systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }

        onDispose {
            activity?.requestedOrientation = originalOrientation ?: ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            if (window != null) {
                WindowCompat.setDecorFitsSystemWindows(window, true)
                WindowInsetsControllerCompat(window, view).apply {
                    show(androidx.core.view.WindowInsetsCompat.Type.systemBars())
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
            ) {
                viewModel.onEvent(PlayerEvent.ToggleControls)
            }
    ) {
        if (uiState.isBuffering) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = ColorPrimary,
            )
        }

        if (uiState.isControlsVisible && !uiState.isLocked) {
            PlayerControls(
                state = uiState,
                onEvent = viewModel::onEvent,
                onBack = onBack,
            )
        }

        if (uiState.isLocked) {
            IconButton(
                onClick = { viewModel.onEvent(PlayerEvent.ToggleLock) },
                modifier = Modifier.align(Alignment.Center),
            ) {
                Icon(
                    Icons.Filled.Lock,
                    contentDescription = "Unlock",
                    tint = Color.White.copy(alpha = 0.7f),
                    modifier = Modifier.size(48.dp),
                )
            }
        }
    }
}

@Composable
private fun PlayerControls(
    state: PlayerUiState,
    onEvent: (PlayerEvent) -> Unit,
    onBack: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = 0.5f))
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Kembali", tint = Color.White)
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = state.donghua?.title ?: "",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                )
                Text(
                    text = state.currentEpisode?.let { "Ep ${it.episodeNumber.toInt()}" } ?: "",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.7f),
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = { onEvent(PlayerEvent.PlayPrevious) }) {
                Icon(Icons.Filled.SkipPrevious, "Previous", tint = Color.White, modifier = Modifier.size(36.dp))
            }
            Spacer(modifier = Modifier.width(24.dp))
            IconButton(onClick = { onEvent(PlayerEvent.SkipBackward) }) {
                Icon(Icons.Filled.Replay10, "Rewind 10s", tint = Color.White, modifier = Modifier.size(36.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            IconButton(onClick = { onEvent(PlayerEvent.TogglePlayPause) }) {
                Icon(
                    if (state.isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                    if (state.isPlaying) "Pause" else "Play",
                    tint = Color.White,
                    modifier = Modifier.size(56.dp),
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            IconButton(onClick = { onEvent(PlayerEvent.SkipForward) }) {
                Icon(Icons.Filled.Forward10, "Forward 10s", tint = Color.White, modifier = Modifier.size(36.dp))
            }
            Spacer(modifier = Modifier.width(24.dp))
            IconButton(onClick = { onEvent(PlayerEvent.PlayNext) }) {
                Icon(Icons.Filled.SkipNext, "Next", tint = Color.White, modifier = Modifier.size(36.dp))
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = 0.5f))
                .padding(8.dp),
        ) {
            var sliderPosition by remember { mutableFloatStateOf(0f) }
            var isDragging by remember { mutableStateOf(false) }

            LaunchedEffect(state.positionMs, state.durationMs) {
                if (!isDragging && state.durationMs > 0) {
                    sliderPosition = state.positionMs.toFloat() / state.durationMs
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = formatMs(state.positionMs),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White,
                )
                Slider(
                    value = sliderPosition,
                    onValueChange = { fraction ->
                        isDragging = true
                        sliderPosition = fraction
                    },
                    onValueChangeFinished = {
                        isDragging = false
                        onEvent(PlayerEvent.SeekTo((sliderPosition * state.durationMs).toLong()))
                    },
                    modifier = Modifier.weight(1f),
                    colors = SliderDefaults.colors(
                        thumbColor = ColorPrimary,
                        activeTrackColor = ColorPrimary,
                    ),
                )
                Text(
                    text = formatMs(state.durationMs),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White,
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                IconButton(onClick = { onEvent(PlayerEvent.ToggleLock) }) {
                    Icon(Icons.Filled.LockOpen, "Lock", tint = Color.White, modifier = Modifier.size(24.dp))
                }
                var showSpeed by remember { mutableStateOf(false) }
                Box {
                    TextButton(onClick = { showSpeed = true }) {
                        Text(
                            text = "${state.playbackSpeed}x",
                            color = Color.White,
                            style = MaterialTheme.typography.labelMedium,
                        )
                    }
                    DropdownMenu(expanded = showSpeed, onDismissRequest = { showSpeed = false }) {
                        listOf(0.5f, 0.75f, 1f, 1.25f, 1.5f, 2f).forEach { speed ->
                            DropdownMenuItem(
                                text = { Text("${speed}x") },
                                onClick = {
                                    onEvent(PlayerEvent.SetPlaybackSpeed(speed))
                                    showSpeed = false
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun formatMs(ms: Long): String {
    val totalSeconds = ms / 1000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    return if (hours > 0) String.format("%d:%02d:%02d", hours, minutes, seconds)
    else String.format("%02d:%02d", minutes, seconds)
}
