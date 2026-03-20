package com.azhua.ext.anichin

import android.util.Log
import com.azhua.core.contracts.Source
import com.azhua.core.contracts.models.Anime
import com.azhua.core.contracts.models.AnimeStatus
import com.azhua.core.contracts.models.AnimeType
import com.azhua.core.contracts.models.Episode
import com.azhua.core.contracts.models.VideoQuality
import com.azhua.core.contracts.models.VideoStream
import com.azhua.ext.anichin.network.AnichinClient
import com.azhua.ext.anichin.parser.AnimeDetailParser
import com.azhua.ext.anichin.parser.AnimeListParser
import com.azhua.ext.anichin.parser.EpisodeParser
import com.azhua.ext.anichin.parser.VideoStreamParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * AnichinSource - Artefak Penjelajah Anichin
 *
 * Implementasi Source interface untuk website Anichin (anichin.moe)
 * Versi Safety-First dengan try-catch di setiap operasi
 */
class AnichinSource : Source {

    companion object {
        private const val TAG = "AnichinSource"
    }

    override val id: String = "anichin"
    override val name: String = "Anichin"
    override val baseUrl: String = "https://anichin.moe"
    override val version: String = "2.0.18"
    override val language: String = "id"

    private val client = AnichinClient()
    private val listParser = AnimeListParser(client)
    private val detailParser = AnimeDetailParser(client)
    private val episodeParser = EpisodeParser(client)
    private val videoParser = VideoStreamParser(client)

    /**
     * Mengambil daftar anime populer
     */
    override suspend fun getPopularAnime(page: Int): List<Anime> = withContext(Dispatchers.IO) {
        try {
            val url = if (page > 1) "$baseUrl/page/$page/" else baseUrl
            Log.d(TAG, "Fetching popular anime from: $url")

            val document = client.getDocument(url)
            val animeList = listParser.parseAnimeList(document)

            Log.d(TAG, "Found ${animeList.size} popular anime")
            return@withContext animeList
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching popular anime: ${e.message}", e)
            return@withContext emptyList()
        }
    }

    /**
     * Mengambil daftar anime terbaru
     */
    override suspend fun getLatestAnime(page: Int): List<Anime> = withContext(Dispatchers.IO) {
        try {
            val url = "$baseUrl/anime/?status=ongoing&order=update"
            val finalUrl = if (page > 1) "$url&page=$page" else url

            Log.d(TAG, "Fetching latest anime from: $finalUrl")

            val document = client.getDocument(finalUrl)
            val animeList = listParser.parseAnimeList(document)

            Log.d(TAG, "Found ${animeList.size} latest anime")
            return@withContext animeList
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching latest anime: ${e.message}", e)
            return@withContext emptyList()
        }
    }

    /**
     * Mengambil daftar anime upcoming
     */
    override suspend fun getUpcomingAnime(page: Int): List<Anime> = withContext(Dispatchers.IO) {
        try {
            val url = "$baseUrl/anime/?status=upcoming"
            Log.d(TAG, "Fetching upcoming anime from: $url")

            val document = client.getDocument(url)
            val animeList = listParser.parseAnimeList(document)

            return@withContext animeList
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching upcoming anime: ${e.message}", e)
            return@withContext emptyList()
        }
    }

    /**
     * Mengambil daftar anime completed
     */
    override suspend fun getCompletedAnime(page: Int): List<Anime> = withContext(Dispatchers.IO) {
        try {
            val url = "$baseUrl/anime/?status=completed"
            val finalUrl = if (page > 1) "$url&page=$page" else url
            Log.d(TAG, "Fetching completed anime from: $finalUrl")

            val document = client.getDocument(finalUrl)
            val animeList = listParser.parseAnimeList(document)

            return@withContext animeList
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching completed anime: ${e.message}", e)
            return@withContext emptyList()
        }
    }

    /**
     * Mengambil daftar Anime bertipe Movie (Layar Lebar)
     * 
     * URL: https://anichin.moe/anime/?type=Movie&order=update
     * Note: Movie hanya memiliki 1 halaman (17 items), pagination mengembalikan 0
     */
    override suspend fun getMovies(page: Int): List<Anime> = withContext(Dispatchers.IO) {
        try {
            // Movie hanya ada di halaman 1, halaman > 1 kosong
            if (page > 1) {
                Log.d(TAG, "Movie only has 1 page, returning empty for page $page")
                return@withContext emptyList()
            }
            
            val url = "$baseUrl/anime/?type=Movie&order=update"
            Log.d(TAG, "Fetching movies from: $url")

            val document = client.getDocument(url)
            val animeList = listParser.parseAnimeList(document)

            Log.d(TAG, "Found ${animeList.size} movies")
            return@withContext animeList
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching movies: ${e.message}", e)
            return@withContext emptyList()
        }
    }

    /**
     * Mencari anime
     */
    override suspend fun searchAnime(
        query: String,
        page: Int,
        filters: Map<String, String>
    ): List<Anime> = withContext(Dispatchers.IO) {
        if (query.isBlank()) return@withContext emptyList()

        try {
            val url = "$baseUrl/"
            Log.d(TAG, "Searching for: $query")

            val formData = mutableMapOf("s" to query)
            filters["genre"]?.let { formData["genre"] = it }
            filters["status"]?.let { formData["status"] = it }
            filters["type"]?.let { formData["type"] = it }

            val document = client.postDocument(url, formData)
            val animeList = listParser.parseSearchResults(document)

            Log.d(TAG, "Found ${animeList.size} search results")
            return@withContext animeList
        } catch (e: Exception) {
            Log.e(TAG, "Error searching anime: ${e.message}", e)
            return@withContext emptyList()
        }
    }

    /**
     * Mendapatkan detail lengkap anime
     */
    override suspend fun getAnimeDetails(animeId: String): Anime = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "Fetching anime details from: $animeId")
            
            if (animeId.isBlank()) {
                Log.e(TAG, "Empty animeId provided")
                return@withContext Anime(
                    id = "error",
                    title = "Error: Empty URL",
                    sourceUrl = ""
                )
            }

            val document = client.getDocument(animeId)
            
            if (document == null) {
                Log.e(TAG, "Failed to load document from: $animeId")
                return@withContext Anime(
                    id = animeId,
                    title = "Error: Failed to load page",
                    sourceUrl = animeId
                )
            }

            // Parse detail dengan safety
            val anime = try {
                detailParser.parseAnimeDetail(document, animeId)
            } catch (e: Exception) {
                Log.e(TAG, "Error parsing anime detail", e)
                null
            }
            
            if (anime == null) {
                return@withContext Anime(
                    id = animeId,
                    title = "Error Loading Detail",
                    sourceUrl = animeId
                )
            }

            // Parse episodes dengan safety
            val episodes = try {
                episodeParser.parseEpisodeList(document, animeId)
            } catch (e: Exception) {
                Log.e(TAG, "Error parsing episodes", e)
                emptyList()
            }

            Log.d(TAG, "Successfully loaded ${episodes.size} episodes for ${anime.title}")
            return@withContext anime.copy(episodes = episodes)
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching anime details: ${e.message}", e)
            return@withContext Anime(
                id = animeId,
                title = "Error: ${e.message}",
                sourceUrl = animeId
            )
        }
    }

    /**
     * Mendapatkan daftar episode
     */
    override suspend fun getEpisodes(animeId: String): List<Episode> = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "Fetching episodes from: $animeId")

            if (animeId.isBlank()) {
                return@withContext emptyList()
            }

            val document = client.getDocument(animeId)
            val episodes = episodeParser.parseEpisodeList(document, animeId)

            Log.d(TAG, "Found ${episodes.size} episodes")
            return@withContext episodes
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching episodes: ${e.message}", e)
            return@withContext emptyList()
        }
    }

    /**
     * Mendapatkan link streaming video
     */
    override suspend fun getVideoStreams(episodeId: String): List<VideoStream> = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "Fetching video streams from: $episodeId")

            if (episodeId.isBlank()) {
                return@withContext emptyList()
            }

            val document = client.getDocument(episodeId)

            if (document == null) {
                Log.e(TAG, "Failed to load episode page")
                return@withContext emptyList()
            }

            var streams = videoParser.parseVideoStreams(document, episodeId)

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
            return@withContext streams
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching video streams: ${e.message}", e)
            return@withContext emptyList()
        }
    }

    /**
     * Mendapatkan daftar genre
     */
    override suspend fun getGenres(): List<String> = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "Fetching genres")

            val document = client.getDocument(baseUrl)
            val genres = mutableListOf<String>()

            document?.select("ul.genre-menu li a, .genre-item a, a[href*=genre]")?.forEach { element ->
                try {
                    val genreName = element.text().trim()
                    if (genreName.isNotEmpty() && genreName.length < 30) {
                        genres.add(genreName)
                    }
                } catch (e: Exception) {
                    // Continue
                }
            }

            return@withContext genres.distinct()
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching genres: ${e.message}", e)
            return@withContext emptyList()
        }
    }

    /**
     * Mendapatkan anime berdasarkan genre
     */
    override suspend fun getAnimeByGenre(genre: String, page: Int): List<Anime> = withContext(Dispatchers.IO) {
        try {
            val url = if (page > 1) "$baseUrl/genres/$genre/page/$page/" else "$baseUrl/genres/$genre/"
            Log.d(TAG, "Fetching anime by genre from: $url")

            val document = client.getDocument(url)
            val animeList = listParser.parseAnimeList(document)

            Log.d(TAG, "Found ${animeList.size} anime for genre: $genre")
            return@withContext animeList
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching anime by genre: ${e.message}", e)
            return@withContext emptyList()
        }
    }
}
