package com.azhua.feature.library

sealed class LibraryUiState {
    data object Loading : LibraryUiState()
    data object Empty : LibraryUiState()
    data class Error(val message: String) : LibraryUiState()
    data class Success(val donghuaCount: Int = 0) : LibraryUiState()
}
