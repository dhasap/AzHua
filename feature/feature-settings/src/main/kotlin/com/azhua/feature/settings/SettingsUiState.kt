package com.azhua.feature.settings

sealed class SettingsUiState {
    data object Loading : SettingsUiState()
    data object Empty : SettingsUiState()
    data class Error(val message: String) : SettingsUiState()
    data class Success(val data: Any? = null) : SettingsUiState()
}
