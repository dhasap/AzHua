package com.azhua.feature.extensions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azhua.app.extension.ExtensionManager
import com.azhua.core.model.Extension
import com.azhua.core.model.ExtensionStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExtensionViewModel @Inject constructor(
    private val extensionManager: ExtensionManager,
) : ViewModel() {

    private val _uiState = MutableStateFlow<ExtensionUiState>(ExtensionUiState.Loading)
    val uiState: StateFlow<ExtensionUiState> = _uiState.asStateFlow()

    private val _activeTab = MutableStateFlow(ExtensionTab.INSTALLED)

    init {
        loadExtensions()
    }

    private fun loadExtensions() {
        viewModelScope.launch {
            try {
                extensionManager.initialize()

                combine(
                    extensionManager.installedExtensions,
                    extensionManager.availableExtensions,
                    extensionManager.updatableExtensions,
                    _activeTab,
                ) { installed, available, updatable, tab ->
                    ExtensionUiState.Success(
                        installed = installed,
                        available = available,
                        updatable = updatable,
                        activeTab = tab,
                    ) as ExtensionUiState
                }.catch { e ->
                    emit(ExtensionUiState.Error(e.message ?: "Unknown error"))
                }.collect { state ->
                    _uiState.value = state
                }
            } catch (e: Exception) {
                _uiState.value = ExtensionUiState.Error(e.message ?: "Failed to load extensions")
            }
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
                viewModelScope.launch {
                    _uiState.update {
                        if (it is ExtensionUiState.Success) {
                            it.copy(installed = it.installed.map { item ->
                                if (item.extension.id == event.extensionId) item.copy(isLoading = true) else item
                            })
                        } else it
                    }
                    extensionManager.installExtension(event.extensionId)
                    loadExtensions()
                }
            }
            is ExtensionEvent.Uninstall -> {
                viewModelScope.launch {
                    extensionManager.uninstallExtension(event.extensionId)
                    loadExtensions()
                }
            }
            is ExtensionEvent.Update -> {
                viewModelScope.launch {
                    extensionManager.installExtension(event.extensionId)
                    loadExtensions()
                }
            }
            ExtensionEvent.UpdateAll -> {
                viewModelScope.launch {
                    val current = _uiState.value
                    if (current is ExtensionUiState.Success) {
                        current.updatable.forEach { item ->
                            extensionManager.installExtension(item.extension.id)
                        }
                        loadExtensions()
                    }
                }
            }
            ExtensionEvent.Retry -> {
                _uiState.value = ExtensionUiState.Loading
                loadExtensions()
            }
        }
    }
}
