package com.azhua.app.extension

import android.content.Context
import android.content.pm.PackageManager
import dalvik.system.DexClassLoader
import com.azhua.extension.api.AzExtension
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExtensionLoader @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val extensionsDir = File(context.getExternalFilesDir(null), "extensions")
    private val loadedExtensions = ConcurrentHashMap<String, AzExtension>()

    init {
        extensionsDir.mkdirs()
    }

    @Synchronized
    fun loadAllExtensions(): List<LoadedExtension> {
        val extensions = mutableListOf<LoadedExtension>()
        loadedExtensions.clear()

        extensionsDir.listFiles()?.filter { it.extension == "apk" }?.forEach { apkFile ->
            try {
                val ext = loadExtension(apkFile)
                if (ext != null) {
                    extensions.add(ext)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return extensions
    }

    @Synchronized
    fun loadExtension(apkFile: File): LoadedExtension? {
        try {
            val optimizedDir = File(context.cacheDir, "dex_opt")
            optimizedDir.mkdirs()

            val dexClassLoader = DexClassLoader(
                apkFile.absolutePath,
                optimizedDir.absolutePath,
                null,
                context.classLoader
            )

            val packageInfo = context.packageManager.getPackageArchiveInfo(
                apkFile.absolutePath,
                PackageManager.GET_META_DATA
            ) ?: return null

            val metadata = packageInfo.applicationInfo?.metaData
            val extensionId = metadata?.getString("com.azhua.extension.id") ?: return null
            val extensionVersion = metadata.getInt("com.azhua.extension.versionId", 0)
            val extensionLang = metadata.getString("com.azhua.extension.lang") ?: "en"

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

    fun getExtension(extensionId: String): AzExtension? = loadedExtensions[extensionId]

    fun getLoadedExtensions(): Map<String, AzExtension> = loadedExtensions.toMap()

    @Synchronized
    fun installExtension(apkFile: File): Boolean {
        try {
            val destFile = File(extensionsDir, apkFile.name)
            apkFile.copyTo(destFile, overwrite = true)
            val loaded = loadExtension(destFile)
            return loaded != null
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    @Synchronized
    fun uninstallExtension(extensionId: String): Boolean {
        try {
            loadedExtensions.remove(extensionId)
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

    private fun findExtensionClass(apkFile: File): String? {
        val packageInfo = context.packageManager.getPackageArchiveInfo(
            apkFile.absolutePath,
            PackageManager.GET_META_DATA
        ) ?: return null

        val packageName = packageInfo.packageName
        val commonClassNames = listOf(
            "${packageName}.Extension",
            "${packageName}.${packageName.split(".").last().replaceFirstChar { it.uppercase() }}Extension",
            "${packageName}.Main",
        )

        return commonClassNames.firstOrNull()
    }
}

data class LoadedExtension(
    val extension: AzExtension,
    val apkFile: File,
    val isLoaded: Boolean,
)
