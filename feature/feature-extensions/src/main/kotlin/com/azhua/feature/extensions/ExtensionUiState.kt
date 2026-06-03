package com.azhua.feature.extensions

sealed class ExtensionUiState {
    data object Loading : ExtensionUiState()
    data object Empty : ExtensionUiState()
    data class Error(val message: String) : ExtensionUiState()
    data class Success(val data: Any? = null) : ExtensionUiState()
}
