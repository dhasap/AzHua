package com.azhua.feature.extensions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azhua.core.model.Extension
import com.azhua.core.model.ExtensionStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExtensionViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow<ExtensionUiState>(ExtensionUiState.Loading)
    val uiState: StateFlow<ExtensionUiState> = _uiState.asStateFlow()

    private val _activeTab = MutableStateFlow(ExtensionTab.INSTALLED)

    init {
        loadExtensions()
    }

    private fun loadExtensions() {
        viewModelScope.launch {
            // TODO: Load from extension manager
            _uiState.value = ExtensionUiState.Success(
                installed = emptyList(),
                available = listOf(
                    ExtensionItem(
                        extension = Extension(
                            id = "anichin",
                            name = "Anichin",
                            packageName = "com.azhua.ext.anichin",
                            versionName = "1.0.0",
                            versionCode = 1,
                            lang = "id",
                            baseUrl = "https://anichin.top",
                            isInstalled = false,
                        ),
                        status = ExtensionStatus.AVAILABLE,
                    ),
                ),
                updatable = emptyList(),
                activeTab = _activeTab.value,
            )
        }
    }

    fun onEvent(event: ExtensionEvent) {
        when (event) {
            is ExtensionEvent.TabChanged -> {
                _activeTab.value = event.tab
                val current = _uiState.value
                if (current is ExtensionUiState.Success) {
                    _uiState.value = current.copy(activeTab = event.tab)
                }
            }
            is ExtensionEvent.Install -> {
                // TODO: Install extension
            }
            is ExtensionEvent.Uninstall -> {
                // TODO: Uninstall extension
            }
            is ExtensionEvent.Update -> {
                // TODO: Update extension
            }
            ExtensionEvent.UpdateAll -> {
                // TODO: Update all extensions
            }
            ExtensionEvent.Retry -> {
                _uiState.value = ExtensionUiState.Loading
                loadExtensions()
            }
        }
    }
}
