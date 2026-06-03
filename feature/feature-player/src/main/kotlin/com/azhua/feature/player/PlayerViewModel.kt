package com.azhua.feature.player

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azhua.data.repository.DonghuaRepository
import com.azhua.data.repository.EpisodeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val donghuaRepository: DonghuaRepository,
    private val episodeRepository: EpisodeRepository,
) : ViewModel() {

    private val donghuaId: Long = savedStateHandle["extra_donghua_id"] ?: 0L
    private val episodeId: Long = savedStateHandle["extra_episode_id"] ?: 0L

    private val _uiState = MutableStateFlow(PlayerUiState())
    val uiState: StateFlow<PlayerUiState> = _uiState.asStateFlow()

    private var controlsHideJob: Job? = null
    private var progressSaveJob: Job? = null

    // Non-cancellable scope for saving progress on exit
    private val saveScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    init {
        if (donghuaId > 0) {
            loadData()
            startPeriodicProgressSave()
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            donghuaRepository.getDonghuaById(donghuaId).collect { donghua ->
                donghua?.let {
                    _uiState.update { state -> state.copy(donghua = it) }
                }
            }
        }
        viewModelScope.launch {
            episodeRepository.getEpisodesByDonghua(donghuaId).collect { episodes ->
                _uiState.update { it.copy(episodeList = episodes) }
                val current = episodes.find { ep -> ep.id == episodeId }
                current?.let { ep ->
                    _uiState.update { state ->
                        state.copy(
                            currentEpisode = ep,
                            positionMs = ep.lastWatchMs,
                            durationMs = ep.durationMs,
                        )
                    }
                }
            }
        }
    }

    private fun startPeriodicProgressSave() {
        progressSaveJob = viewModelScope.launch {
            while (isActive) {
                delay(5000)
                saveCurrentProgressSync()
            }
        }
    }

    /**
     * Synchronous progress save - does NOT use viewModelScope.
     * Called from periodic save and onCleared.
     */
    private fun saveCurrentProgressSync() {
        val state = _uiState.value
        if (state.currentEpisode != null && state.positionMs > 0) {
            // Use runBlocking on saveScope to ensure save completes
            saveScope.launch {
                episodeRepository.updateWatchProgress(
                    episodeId = state.currentEpisode.id,
                    positionMs = state.positionMs,
                    isWatched = state.positionMs >= state.durationMs * 0.9,
                )
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
                saveCurrentProgressSync()
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
                saveCurrentProgressSync()
                val current = _uiState.value.currentEpisode ?: return
                val next = _uiState.value.episodeList
                    .filter { it.episodeNumber > current.episodeNumber }
                    .minByOrNull { it.episodeNumber }
                next?.let { onEvent(PlayerEvent.PlayEpisode(it.id)) }
            }
            PlayerEvent.PlayPrevious -> {
                saveCurrentProgressSync()
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
        progressSaveJob?.cancel()
        // Save final progress using non-cancellable scope
        val state = _uiState.value
        if (state.currentEpisode != null && state.positionMs > 0) {
            runBlocking {
                episodeRepository.updateWatchProgress(
                    episodeId = state.currentEpisode.id,
                    positionMs = state.positionMs,
                    isWatched = state.positionMs >= state.durationMs * 0.9,
                )
            }
        }
        saveScope.cancel()
    }
}
