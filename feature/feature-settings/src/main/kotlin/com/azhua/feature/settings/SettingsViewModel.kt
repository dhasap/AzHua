package com.azhua.feature.settings

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.ThemeChanged -> _uiState.update { it.copy(theme = event.theme) }
            is SettingsEvent.GridColumnsChanged -> _uiState.update { it.copy(gridColumns = event.columns) }
            is SettingsEvent.TextSizeChanged -> _uiState.update { it.copy(textSize = event.size) }
            is SettingsEvent.DefaultQualityChanged -> _uiState.update { it.copy(defaultQuality = event.quality) }
            is SettingsEvent.AutoPlayNextChanged -> _uiState.update { it.copy(autoPlayNext = event.enabled) }
            is SettingsEvent.SkipIntroDurationChanged -> _uiState.update { it.copy(skipIntroDuration = event.duration) }
            is SettingsEvent.UpdateOnOpenChanged -> _uiState.update { it.copy(updateOnOpen = event.enabled) }
            is SettingsEvent.NotifyNewEpisodesChanged -> _uiState.update { it.copy(notifyNewEpisodes = event.enabled) }
            SettingsEvent.ClearCache -> { /* TODO */ }
            SettingsEvent.CreateBackup -> { /* TODO */ }
            SettingsEvent.RestoreBackup -> { /* TODO */ }
        }
    }
}
