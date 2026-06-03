package com.azhua.app.extension

import com.azhua.core.model.Extension
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for fetching available extensions from remote sources.
 */
@Singleton
class ExtensionRepository @Inject constructor(
    private val okHttpClient: OkHttpClient,
) {
    companion object {
        // Default extension repository URL
        private const val REPO_URL = "https://raw.githubusercontent.com/dhasap/azhua-extensions/main/index.json"
        private const val REPO_BASE_URL = "https://raw.githubusercontent.com/dhasap/azhua-extensions/main/"
    }

    private val gson = Gson()

    /**
     * Fetch list of available extensions from the repository.
     */
    suspend fun getAvailableExtensions(): List<Extension> = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url(REPO_URL)
                .build()

            val response = okHttpClient.newCall(request).execute()
            if (!response.isSuccessful) {
                return@withContext emptyList()
            }

            val body = response.body?.string() ?: return@withContext emptyList()
            val type = object : TypeToken<List<ExtensionJson>>() {}.type
            val extensions: List<ExtensionJson> = gson.fromJson(body, type)

            extensions.map { it.toDomain() }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    /**
     * Download an extension APK file.
     */
    suspend fun downloadExtension(extension: Extension): File = withContext(Dispatchers.IO) {
        val apkUrl = "${REPO_BASE_URL}${extension.id}/latest.apk"
        val request = Request.Builder()
            .url(apkUrl)
            .build()

        val response = okHttpClient.newCall(request).execute()
        if (!response.isSuccessful) {
            throw Exception("Failed to download extension: ${response.code}")
        }

        val tempFile = File.createTempFile("ext_", ".apk")
        val body = response.body ?: throw Exception("Empty response body")

        FileOutputStream(tempFile).use { fos ->
            body.byteStream().use { inputStream ->
                inputStream.copyTo(fos)
            }
        }

        tempFile
    }

    /**
     * JSON representation of an extension from the repository.
     */
    private data class ExtensionJson(
        val id: String,
        val name: String,
        val pkg: String,
        val version: String,
        val versionCode: Int,
        val lang: String,
        val baseUrl: String,
        val icon: String = "",
        val nsfw: Boolean = false,
    ) {
        fun toDomain(): Extension {
            return Extension(
                id = id,
                name = name,
                packageName = pkg,
                versionName = version,
                versionCode = versionCode,
                lang = lang,
                baseUrl = baseUrl,
                iconUrl = icon,
                isInstalled = false,
                hasUpdate = false,
                isNsfw = nsfw,
            )
        }
    }
}
