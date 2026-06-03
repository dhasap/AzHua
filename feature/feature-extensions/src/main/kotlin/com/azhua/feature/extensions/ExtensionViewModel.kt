package com.azhua.feature.extensions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExtensionViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow<ExtensionUiState>(ExtensionUiState.Loading)
    val uiState: StateFlow<ExtensionUiState> = _uiState.asStateFlow()

    init { loadData() }

    private fun loadData() {
        viewModelScope.launch { _uiState.value = ExtensionUiState.Empty }
    }

    fun retry() {
        _uiState.value = ExtensionUiState.Loading
        loadData()
    }
}
