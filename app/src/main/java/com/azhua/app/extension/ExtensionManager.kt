package com.azhua.app.extension

import com.azhua.core.model.Extension
import com.azhua.core.model.ExtensionStatus
import com.azhua.extension.api.AzExtension
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manages the lifecycle of extensions - loading, updating, and providing access.
 */
@Singleton
class ExtensionManager @Inject constructor(
    private val extensionLoader: ExtensionLoader,
    private val extensionRepository: ExtensionRepository,
) {
    private val _installedExtensions = MutableStateFlow<List<ExtensionItem>>(emptyList())
    val installedExtensions: StateFlow<List<ExtensionItem>> = _installedExtensions.asStateFlow()

    private val _availableExtensions = MutableStateFlow<List<ExtensionItem>>(emptyList())
    val availableExtensions: StateFlow<List<ExtensionItem>> = _availableExtensions.asStateFlow()

    private val _updatableExtensions = MutableStateFlow<List<ExtensionItem>>(emptyList())
    val updatableExtensions: StateFlow<List<ExtensionItem>> = _updatableExtensions.asStateFlow()

    /**
     * Initialize - load all installed extensions and fetch available ones.
     */
    suspend fun initialize() = withContext(Dispatchers.IO) {
        loadInstalledExtensions()
        fetchAvailableExtensions()
    }

    /**
     * Load all installed extensions from disk.
     */
    private fun loadInstalledExtensions() {
        val loaded = extensionLoader.loadAllExtensions()
        _installedExtensions.value = loaded.map { loadedExt ->
            ExtensionItem(
                extension = loadedExt.toModel(),
                status = ExtensionStatus.INSTALLED,
            )
        }
    }

    /**
     * Fetch available extensions from the repository.
     */
    private suspend fun fetchAvailableExtensions() {
        try {
            val available = extensionRepository.getAvailableExtensions()
            val installedIds = _installedExtensions.value.map { it.extension.id }.toSet()

            _availableExtensions.value = available
                .filter { it.id !in installedIds }
                .map { ExtensionItem(extension = it, status = ExtensionStatus.AVAILABLE) }

            // Check for updates
            val updatable = _installedExtensions.value.filter { installed ->
                available.any { available ->
                    available.id == installed.extension.id &&
                    available.versionCode > installed.extension.versionCode
                }
            }
            _updatableExtensions.value = updatable.map {
                it.copy(status = ExtensionStatus.UPDATE_AVAILABLE)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Install an extension by ID.
     */
    suspend fun installExtension(extensionId: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val available = _availableExtensions.value.find { it.extension.id == extensionId }
                ?: return@withContext false

            val apkFile = extensionRepository.downloadExtension(available.extension)
            val success = extensionLoader.installExtension(apkFile)

            if (success) {
                loadInstalledExtensions()
                fetchAvailableExtensions()
            }
            success
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Uninstall an extension by ID.
     */
    suspend fun uninstallExtension(extensionId: String): Boolean = withContext(Dispatchers.IO) {
        val success = extensionLoader.uninstallExtension(extensionId)
        if (success) {
            loadInstalledExtensions()
            fetchAvailableExtensions()
        }
        success
    }

    /**
     * Get a loaded extension instance by ID.
     */
    fun getExtension(extensionId: String): AzExtension? {
        return extensionLoader.getExtension(extensionId)
    }

    /**
     * Get all loaded extension instances.
     */
    fun getAllExtensions(): Map<String, AzExtension> {
        return extensionLoader.getLoadedExtensions()
    }

    /**
     * Get all installed extension IDs.
     */
    fun getInstalledExtensionIds(): Set<String> {
        return _installedExtensions.value.map { it.extension.id }.toSet()
    }

    /**
     * Refresh - reload everything.
     */
    suspend fun refresh() {
        loadInstalledExtensions()
        fetchAvailableExtensions()
    }
}

/**
 * Extension item for UI display.
 */
data class ExtensionItem(
    val extension: Extension,
    val status: ExtensionStatus,
    val isLoading: Boolean = false,
)

/**
 * Convert LoadedExtension to domain model.
 */
private fun LoadedExtension.toModel(): Extension {
    return Extension(
        id = extension.id,
        name = extension.name,
        packageName = extension.javaClass.name,
        versionName = "${extension.versionId}",
        versionCode = extension.versionId,
        lang = extension.lang,
        baseUrl = extension.baseUrl,
        iconUrl = extension.iconUrl,
        isInstalled = true,
        hasUpdate = false,
    )
}
