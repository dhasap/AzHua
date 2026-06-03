package com.azhua.feature.discover

sealed class DiscoverUiState {
    data object Loading : DiscoverUiState()
    data object Empty : DiscoverUiState()
    data class Error(val message: String) : DiscoverUiState()
    data class Success(val data: Any? = null) : DiscoverUiState()
}
