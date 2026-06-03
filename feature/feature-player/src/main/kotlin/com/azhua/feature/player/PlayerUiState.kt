package com.azhua.feature.player

sealed class PlayerUiState {
    data object Loading : PlayerUiState()
    data object Empty : PlayerUiState()
    data class Error(val message: String) : PlayerUiState()
    data class Success(val data: Any? = null) : PlayerUiState()
}
