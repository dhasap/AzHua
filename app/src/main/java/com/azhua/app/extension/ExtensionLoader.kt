package com.azhua.app.extension

import android.content.Context
import android.content.pm.PackageManager
import android.os.Environment
import dalvik.system.DexClassLoader
import com.azhua.extension.api.AzExtension
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Loads extension APKs using DexClassLoader.
 * Extensions are stored in the app's external files directory.
 */
@Singleton
class ExtensionLoader @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val extensionsDir = File(context.getExternalFilesDir(null), "extensions")
    private val loadedExtensions = mutableMapOf<String, AzExtension>()

    init {
        extensionsDir.mkdirs()
    }

    /**
     * Load all installed extensions from the extensions directory.
     */
    fun loadAllExtensions(): List<LoadedExtension> {
        val extensions = mutableListOf<LoadedExtension>()

        extensionsDir.listFiles()?.filter { it.extension == "apk" }?.forEach { apkFile ->
            try {
                val ext = loadExtension(apkFile)
                if (ext != null) {
                    extensions.add(ext)
                }
            } catch (e: Exception) {
                // Log error but continue loading other extensions
                e.printStackTrace()
            }
        }

        return extensions
    }

    /**
     * Load a single extension from an APK file.
     */
    fun loadExtension(apkFile: File): LoadedExtension? {
        try {
            // Create optimized directory
            val optimizedDir = File(context.cacheDir, "dex_opt")
            optimizedDir.mkdirs()

            // Load the APK
            val dexClassLoader = DexClassLoader(
                apkFile.absolutePath,
                optimizedDir.absolutePath,
                null,
                context.classLoader
            )

            // Read metadata from the APK
            val packageInfo = context.packageManager.getPackageArchiveInfo(
                apkFile.absolutePath,
                PackageManager.GET_META_DATA
            ) ?: return null

            val metadata = packageInfo.applicationInfo?.metaData
            val extensionId = metadata?.getString("com.azhua.extension.id") ?: return null
            val extensionVersion = metadata.getInt("com.azhua.extension.versionId", 0)
            val extensionLang = metadata.getString("com.azhua.extension.lang") ?: "en"

            // Find the extension class
            val className = metadata.getString("com.azhua.extension.class")
                ?: findExtensionClass(apkFile)
                ?: return null

            val extensionClass = dexClassLoader.loadClass(className)
            val extension = extensionClass.newInstance() as? AzExtension ?: return null

            loadedExtensions[extensionId] = extension

            return LoadedExtension(
                extension = extension,
                apkFile = apkFile,
                isLoaded = true,
            )
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    /**
     * Get a loaded extension by ID.
     */
    fun getExtension(extensionId: String): AzExtension? {
        return loadedExtensions[extensionId]
    }

    /**
     * Get all loaded extensions.
     */
    fun getLoadedExtensions(): Map<String, AzExtension> {
        return loadedExtensions.toMap()
    }

    /**
     * Install an extension from a downloaded APK file.
     */
    fun installExtension(apkFile: File): Boolean {
        try {
            val destFile = File(extensionsDir, apkFile.name)
            apkFile.copyTo(destFile, overwrite = true)

            // Try to load it to verify it works
            val loaded = loadExtension(destFile)
            return loaded != null
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    /**
     * Uninstall an extension by ID.
     */
    fun uninstallExtension(extensionId: String): Boolean {
        try {
            loadedExtensions.remove(extensionId)

            // Find and delete the APK file
            extensionsDir.listFiles()?.forEach { apkFile ->
                try {
                    val packageInfo = context.packageManager.getPackageArchiveInfo(
                        apkFile.absolutePath,
                        PackageManager.GET_META_DATA
                    )
                    val id = packageInfo?.applicationInfo?.metaData?.getString("com.azhua.extension.id")
                    if (id == extensionId) {
                        apkFile.delete()
                        return true
                    }
                } catch (e: Exception) {
                    // Continue searching
                }
            }
            return false
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    /**
     * Find the extension class name by scanning the APK.
     */
    private fun findExtensionClass(apkFile: File): String? {
        // Default convention: the extension class is in the main package
        val packageInfo = context.packageManager.getPackageArchiveInfo(
            apkFile.absolutePath,
            PackageManager.GET_META_DATA
        ) ?: return null

        // Look for a class that implements AzExtension
        // This is a simplified approach - in production, use manifest metadata
        val packageName = packageInfo.packageName
        val commonClassNames = listOf(
            "${packageName}.Extension",
            "${packageName}.${packageName.split(".").last().replaceFirstChar { it.uppercase() }}Extension",
            "${packageName}.Main",
        )

        return commonClassNames.firstOrNull()
    }
}

/**
 * Represents a loaded extension.
 */
data class LoadedExtension(
    val extension: AzExtension,
    val apkFile: File,
    val isLoaded: Boolean,
)
