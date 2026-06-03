package com.azhua.feature.player

import com.azhua.core.model.Donghua
import com.azhua.core.model.Episode

data class PlayerUiState(
    val donghua: Donghua? = null,
    val currentEpisode: Episode? = null,
    val episodeList: List<Episode> = emptyList(),
    val isPlaying: Boolean = false,
    val isBuffering: Boolean = false,
    val positionMs: Long = 0,
    val durationMs: Long = 0,
    val bufferedMs: Long = 0,
    val isControlsVisible: Boolean = true,
    val isLocked: Boolean = false,
    val playbackSpeed: Float = 1f,
    val error: String? = null,
)

sealed class PlayerEvent {
    data object TogglePlayPause : PlayerEvent()
    data class SeekTo(val positionMs: Long) : PlayerEvent()
    data object SkipForward : PlayerEvent()
    data object SkipBackward : PlayerEvent()
    data object ToggleControls : PlayerEvent()
    data object ToggleLock : PlayerEvent()
    data class SetPlaybackSpeed(val speed: Float) : PlayerEvent()
    data class PlayEpisode(val episodeId: Long) : PlayerEvent()
    data object PlayNext : PlayerEvent()
    data object PlayPrevious : PlayerEvent()
}
