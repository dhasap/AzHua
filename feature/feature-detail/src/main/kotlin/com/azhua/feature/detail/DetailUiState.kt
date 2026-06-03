package com.azhua.feature.detail

sealed class DetailUiState {
    data object Loading : DetailUiState()
    data object Empty : DetailUiState()
    data class Error(val message: String) : DetailUiState()
    data class Success(val data: Any? = null) : DetailUiState()
}
