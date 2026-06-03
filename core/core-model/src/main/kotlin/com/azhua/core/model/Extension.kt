package com.azhua.core.model

/**
 * Domain model for an Extension.
 */
data class Extension(
    val id: String,
    val name: String,
    val packageName: String,
    val versionName: String,
    val versionCode: Int,
    val lang: String,
    val baseUrl: String,
    val iconUrl: String = "",
    val isInstalled: Boolean = false,
    val hasUpdate: Boolean = false,
    val isNsfw: Boolean = false,
)

enum class ExtensionStatus {
    INSTALLED, AVAILABLE, UPDATE_AVAILABLE, INCOMPATIBLE
}
