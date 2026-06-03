package com.azhua.feature.extensions

import com.azhua.core.model.Extension
import com.azhua.core.model.ExtensionStatus

sealed class ExtensionUiState {
    data object Loading : ExtensionUiState()
    data object Empty : ExtensionUiState()
    data class Error(val message: String) : ExtensionUiState()
    data class Success(
        val installed: List<ExtensionItem>,
        val available: List<ExtensionItem>,
        val updatable: List<ExtensionItem>,
        val activeTab: ExtensionTab,
    ) : ExtensionUiState()
}

data class ExtensionItem(
    val extension: Extension,
    val status: ExtensionStatus,
    val isLoading: Boolean = false,
)

enum class ExtensionTab(val label: String) {
    INSTALLED("Terinstal"),
    UPDATE("Update"),
    AVAILABLE("Tersedia"),
}

sealed class ExtensionEvent {
    data class TabChanged(val tab: ExtensionTab) : ExtensionEvent()
    data class Install(val extensionId: String) : ExtensionEvent()
    data class Uninstall(val extensionId: String) : ExtensionEvent()
    data class Update(val extensionId: String) : ExtensionEvent()
    data object UpdateAll : ExtensionEvent()
    data object Retry : ExtensionEvent()
}
