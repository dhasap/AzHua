package com.azhua.ext.anichin

import android.util.Log
import com.azhua.ext.anichin.network.AnichinClient
import com.azhua.ext.anichin.parser.AnimeDetailParser
import com.azhua.ext.anichin.parser.AnimeListParser
import com.azhua.ext.anichin.parser.EpisodeParser
import com.azhua.ext.anichin.parser.VideoStreamParser
import com.azhua.extension.api.AzExtension
import com.azhua.extension.api.filter.FilterList
import com.azhua.extension.api.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Anichin Extension - Implements AzExtension interface for Anichin.moe
 */
class AnichinSource : AzExtension {

    companion object {
        private const val TAG = "AnichinSource"
    }

    override val id = "anichin"
    override val name = "Anichin"
    override val baseUrl = "https://anichin.moe"
    override val lang = "id"
    override val versionId = 2
    override val iconUrl = "https://anichin.moe/favicon.ico"

    override val supportsLatest = true
    override val supportsSearch = true
    override val supportsFilter = true

    private val client = AnichinClient()
    private val listParser = AnimeListParser(client)
    private val detailParser = AnimeDetailParser(client)
    private val episodeParser = EpisodeParser(client)
    private val videoParser = VideoStreamParser(client)

    override suspend fun getPopularAnime(page: Int): AnimePage = withContext(Dispatchers.IO) {
        try {
            val url = if (page > 1) "$baseUrl/page/$page/" else baseUrl
            Log.d(TAG, "Fetching popular anime from: $url")

            val document = client.getDocument(url)
            val result = listParser.parseAnimeList(document)

            Log.d(TAG, "Found ${result.animes.size} popular anime")
            result
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching popular anime: ${e.message}", e)
            AnimePage(emptyList(), false, page)
        }
    }

    override suspend fun getLatestUpdates(page: Int): AnimePage = withContext(Dispatchers.IO) {
        try {
            val url = "$baseUrl/anime/?status=ongoing&order=update"
            val finalUrl = if (page > 1) "$url&page=$page" else url

            Log.d(TAG, "Fetching latest anime from: $finalUrl")

            val document = client.getDocument(finalUrl)
            val result = listParser.parseAnimeList(document)

            Log.d(TAG, "Found ${result.animes.size} latest anime")
            result
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching latest anime: ${e.message}", e)
            AnimePage(emptyList(), false, page)
        }
    }

    override suspend fun searchAnime(query: String, page: Int, filters: FilterList): AnimePage = withContext(Dispatchers.IO) {
        if (query.isBlank()) return@withContext AnimePage(emptyList(), false, page)

        try {
            Log.d(TAG, "Searching for: $query")

            val formData = mutableMapOf("s" to query)

            // Apply filters
            filters.filters.forEach { filter ->
                when (filter.name) {
                    "Genre" -> filter.state.toString().takeIf { it.isNotEmpty() }?.let { formData["genre"] = it }
                    "Status" -> filter.state.toString().takeIf { it.isNotEmpty() }?.let { formData["status"] = it }
                    "Type" -> filter.state.toString().takeIf { it.isNotEmpty() }?.let { formData["type"] = it }
                }
            }

            val document = client.postDocument("$baseUrl/", formData)
            val result = listParser.parseSearchResults(document)

            Log.d(TAG, "Found ${result.animes.size} search results")
            result
        } catch (e: Exception) {
            Log.e(TAG, "Error searching anime: ${e.message}", e)
            AnimePage(emptyList(), false, page)
        }
    }

    override fun getFilterList(): FilterList {
        return FilterList(
            com.azhua.extension.api.filter.Filter.Select("Status", arrayOf("All", "Ongoing", "Completed")),
            com.azhua.extension.api.filter.Filter.Select("Type", arrayOf("All", "TV", "Movie", "OVA", "ONA")),
            com.azhua.extension.api.filter.Filter.Text("Genre"),
        )
    }

    override suspend fun getAnimeDetails(animeUrl: String): AnimeInfo = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "Fetching anime details from: $animeUrl")

            if (animeUrl.isBlank()) {
                return@withContext AnimeInfo(
                    url = "",
                    title = "Error: Empty URL",
                    coverUrl = "",
                )
            }

            val document = client.getDocument(animeUrl)
            val anime = detailParser.parseAnimeDetail(document, animeUrl)

            Log.d(TAG, "Successfully loaded details for ${anime.title}")
            anime
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching anime details: ${e.message}", e)
            AnimeInfo(
                url = animeUrl,
                title = "Error: ${e.message}",
                coverUrl = "",
            )
        }
    }

    override suspend fun getEpisodeList(animeUrl: String): List<EpisodePage> = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "Fetching episodes from: $animeUrl")

            if (animeUrl.isBlank()) {
                return@withContext emptyList()
            }

            val document = client.getDocument(animeUrl)
            val episodes = episodeParser.parseEpisodeList(document, animeUrl)

            Log.d(TAG, "Found ${episodes.size} episodes")
            episodes
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching episodes: ${e.message}", e)
            emptyList()
        }
    }

    override suspend fun getVideoList(episodeUrl: String): List<VideoUrl> = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "Fetching video streams from: $episodeUrl")

            if (episodeUrl.isBlank()) {
                return@withContext emptyList()
            }

            val document = client.getDocument(episodeUrl)
            var streams = videoParser.parseVideoStreams(document, episodeUrl)

            // If no streams found directly, try server links
            if (streams.isEmpty()) {
                val servers = videoParser.parseServerLinks(document)

                for ((serverName, serverUrl) in servers) {
                    Log.d(TAG, "Trying server: $serverName")
                    try {
                        val serverDoc = client.getDocument(serverUrl)
                        val serverStreams = videoParser.parseVideoStreams(serverDoc, serverUrl)

                        if (serverStreams.isNotEmpty()) {
                            streams = serverStreams
                            break
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "Error trying server $serverName: ${e.message}")
                        continue
                    }
                }
            }

            Log.d(TAG, "Found ${streams.size} video streams")
            streams
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching video streams: ${e.message}", e)
            emptyList()
        }
    }
}
