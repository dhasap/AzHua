package com.azhua.feature.recents

sealed class RecentsUiState {
    data object Loading : RecentsUiState()
    data object Empty : RecentsUiState()
    data class Error(val message: String) : RecentsUiState()
    data class Success(val data: Any? = null) : RecentsUiState()
}
