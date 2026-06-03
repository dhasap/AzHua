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
            // TODO: Inject ExtensionManager when extension system is integrated
            // For now, show available extensions from hardcoded data
            val available = listOf(
                ExtensionItem(
                    extension = Extension(
                        id = "anichin",
                        name = "Anichin",
                        packageName = "com.azhua.ext.anichin",
                        versionName = "2.0.0",
                        versionCode = 2,
                        lang = "id",
                        baseUrl = "https://anichin.moe",
                        isInstalled = false,
                    ),
                    status = ExtensionStatus.AVAILABLE,
                ),
            )

            _uiState.value = ExtensionUiState.Success(
                installed = emptyList(),
                available = available,
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
                // TODO: Implement install via ExtensionManager
            }
            is ExtensionEvent.Uninstall -> {
                // TODO: Implement uninstall via ExtensionManager
            }
            is ExtensionEvent.Update -> {
                // TODO: Implement update via ExtensionManager
            }
            ExtensionEvent.UpdateAll -> {
                // TODO: Implement update all
            }
            ExtensionEvent.Retry -> {
                _uiState.value = ExtensionUiState.Loading
                loadExtensions()
            }
        }
    }
}
