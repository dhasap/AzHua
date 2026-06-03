package com.azhua.feature.recents

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecentsViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow<RecentsUiState>(RecentsUiState.Loading)
    val uiState: StateFlow<RecentsUiState> = _uiState.asStateFlow()

    init { loadData() }

    private fun loadData() {
        viewModelScope.launch { _uiState.value = RecentsUiState.Empty }
    }

    fun retry() {
        _uiState.value = RecentsUiState.Loading
        loadData()
    }
}
