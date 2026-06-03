package com.azhua.feature.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azhua.core.model.Donghua
import com.azhua.core.model.Source
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow<DiscoverUiState>(DiscoverUiState.Loading)
    val uiState: StateFlow<DiscoverUiState> = _uiState.asStateFlow()

    init {
        loadDiscoverData()
    }

    private fun loadDiscoverData() {
        viewModelScope.launch {
            // TODO: Load from extension system
            // For now, show empty state with sample sources
            val sources = listOf(
                Source(id = "anichin", name = "Anichin", lang = "id", baseUrl = "https://anichin.top"),
            )

            _uiState.value = DiscoverUiState.Success(
                sources = sources,
                selectedSource = null,
                trending = emptyList(),
                popular = emptyList(),
                latest = emptyList(),
                searchQuery = "",
                isSearchActive = false,
                searchResults = emptyList(),
            )
        }
    }

    fun onEvent(event: DiscoverEvent) {
        when (event) {
            is DiscoverEvent.SelectSource -> {
                val current = _uiState.value
                if (current is DiscoverUiState.Success) {
                    _uiState.value = current.copy(selectedSource = event.source)
                    // TODO: Load content for selected source
                }
            }
            is DiscoverEvent.SearchQueryChanged -> {
                val current = _uiState.value
                if (current is DiscoverUiState.Success) {
                    _uiState.value = current.copy(searchQuery = event.query)
                    // TODO: Search across sources
                }
            }
            DiscoverEvent.ToggleSearch -> {
                val current = _uiState.value
                if (current is DiscoverUiState.Success) {
                    _uiState.value = current.copy(
                        isSearchActive = !current.isSearchActive,
                        searchQuery = if (current.isSearchActive) "" else current.searchQuery,
                    )
                }
            }
            DiscoverEvent.Refresh -> loadDiscoverData()
            DiscoverEvent.Retry -> {
                _uiState.value = DiscoverUiState.Loading
                loadDiscoverData()
            }
        }
    }
}
