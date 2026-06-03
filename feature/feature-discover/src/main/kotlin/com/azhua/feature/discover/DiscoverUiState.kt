package com.azhua.feature.discover

import com.azhua.core.model.Donghua
import com.azhua.core.model.Source

sealed class DiscoverUiState {
    data object Loading : DiscoverUiState()
    data object Empty : DiscoverUiState()
    data class Error(val message: String) : DiscoverUiState()
    data class Success(
        val sources: List<Source>,
        val selectedSource: Source?,
        val trending: List<Donghua>,
        val popular: List<Donghua>,
        val latest: List<Donghua>,
        val searchQuery: String,
        val isSearchActive: Boolean,
        val searchResults: List<Donghua>,
    ) : DiscoverUiState()
}

sealed class DiscoverEvent {
    data class SelectSource(val source: Source?) : DiscoverEvent()
    data class SearchQueryChanged(val query: String) : DiscoverEvent()
    data object ToggleSearch : DiscoverEvent()
    data object Refresh : DiscoverEvent()
    data object Retry : DiscoverEvent()
}
