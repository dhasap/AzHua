package com.azhua.extension.api

import com.azhua.extension.api.filter.FilterList
import com.azhua.extension.api.model.*

/**
 * Main extension interface that every source extension must implement.
 */
interface AzExtension {
    // Metadata
    val id: String
    val name: String
    val baseUrl: String
    val lang: String
    val versionId: Int
    val iconUrl: String

    // Supported features
    val supportsLatest: Boolean
    val supportsSearch: Boolean
    val supportsFilter: Boolean

    // Browse
    suspend fun getPopularAnime(page: Int): AnimePage
    suspend fun getLatestUpdates(page: Int): AnimePage
    suspend fun searchAnime(query: String, page: Int, filters: FilterList): AnimePage
    fun getFilterList(): FilterList

    // Detail
    suspend fun getAnimeDetails(animeUrl: String): AnimeInfo
    suspend fun getEpisodeList(animeUrl: String): List<EpisodePage>

    // Stream
    suspend fun getVideoList(episodeUrl: String): List<VideoUrl>

    // Settings (optional)
    fun getPreferences(): List<ExtensionPreference> = emptyList()
    fun setupHeaders(): Map<String, String> = emptyMap()
}

/**
 * Extension-specific preference.
 */
data class ExtensionPreference(
    val key: String,
    val title: String,
    val summary: String? = null,
    val entries: Map<String, String> = emptyMap(),
    val defaultValue: String = "",
)
