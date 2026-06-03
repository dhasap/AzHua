package com.azhua.feature.player

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azhua.data.repository.DonghuaRepository
import com.azhua.data.repository.EpisodeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val donghuaRepository: DonghuaRepository,
    private val episodeRepository: EpisodeRepository,
) : ViewModel() {

    private val donghuaId: Long = savedStateHandle["donghuaId"] ?: 0L
    private val episodeId: Long = savedStateHandle["episodeId"] ?: 0L

    private val _uiState = MutableStateFlow(PlayerUiState())
    val uiState: StateFlow<PlayerUiState> = _uiState.asStateFlow()

    private var controlsHideJob: Job? = null

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            donghuaRepository.getDonghuaById(donghuaId).firstOrNull()?.let { donghua ->
                _uiState.update { it.copy(donghua = donghua) }
            }
        }
        viewModelScope.launch {
            episodeRepository.getEpisodesByDonghua(donghuaId).collect { episodes ->
                _uiState.update { it.copy(episodeList = episodes) }
                val current = episodes.find { it.id == episodeId }
                current?.let { ep ->
                    _uiState.update {
                        it.copy(
                            currentEpisode = ep,
                            positionMs = ep.lastWatchMs,
                            durationMs = ep.durationMs,
                        )
                    }
                }
            }
        }
    }

    fun onEvent(event: PlayerEvent) {
        when (event) {
            PlayerEvent.TogglePlayPause -> {
                _uiState.update { it.copy(isPlaying = !it.isPlaying) }
                if (_uiState.value.isPlaying) startControlsTimer()
            }
            is PlayerEvent.SeekTo -> {
                _uiState.update { it.copy(positionMs = event.positionMs) }
            }
            PlayerEvent.SkipForward -> {
                _uiState.update { it.copy(positionMs = (it.positionMs + 10000).coerceAtMost(it.durationMs)) }
            }
            PlayerEvent.SkipBackward -> {
                _uiState.update { it.copy(positionMs = (it.positionMs - 10000).coerceAtLeast(0)) }
            }
            PlayerEvent.ToggleControls -> {
                _uiState.update { it.copy(isControlsVisible = !it.isControlsVisible) }
                if (_uiState.value.isControlsVisible) startControlsTimer()
            }
            PlayerEvent.ToggleLock -> {
                _uiState.update { it.copy(isLocked = !it.isLocked) }
            }
            is PlayerEvent.SetPlaybackSpeed -> {
                _uiState.update { it.copy(playbackSpeed = event.speed) }
            }
            is PlayerEvent.PlayEpisode -> {
                val ep = _uiState.value.episodeList.find { it.id == event.episodeId }
                ep?.let {
                    _uiState.update { state ->
                        state.copy(
                            currentEpisode = it,
                            positionMs = it.lastWatchMs,
                            durationMs = it.durationMs,
                            isPlaying = true,
                        )
                    }
                }
            }
            PlayerEvent.PlayNext -> {
                val current = _uiState.value.currentEpisode ?: return
                val next = _uiState.value.episodeList
                    .filter { it.episodeNumber > current.episodeNumber }
                    .minByOrNull { it.episodeNumber }
                next?.let { onEvent(PlayerEvent.PlayEpisode(it.id)) }
            }
            PlayerEvent.PlayPrevious -> {
                val current = _uiState.value.currentEpisode ?: return
                val prev = _uiState.value.episodeList
                    .filter { it.episodeNumber < current.episodeNumber }
                    .maxByOrNull { it.episodeNumber }
                prev?.let { onEvent(PlayerEvent.PlayEpisode(it.id)) }
            }
        }
    }

    private fun startControlsTimer() {
        controlsHideJob?.cancel()
        controlsHideJob = viewModelScope.launch {
            delay(3000)
            _uiState.update { it.copy(isControlsVisible = false) }
        }
    }

    override fun onCleared() {
        super.onCleared()
        // Save watch progress
        val state = _uiState.value
        if (state.currentEpisode != null && state.positionMs > 0) {
            viewModelScope.launch {
                episodeRepository.updateWatchProgress(
                    episodeId = state.currentEpisode.id,
                    positionMs = state.positionMs,
                    isWatched = state.positionMs >= state.durationMs * 0.9,
                )
            }
        }
    }
}
