package com.azhua.ext.anichin.parser

import android.util.Log
import com.azhua.ext.anichin.network.AnichinClient
import com.azhua.extension.api.model.VideoUrl
import org.jsoup.nodes.Document
import java.util.regex.Pattern

/**
 * Parser for extracting video stream URLs from Anichin episode pages.
 * Handles multiple server providers and video formats.
 */
class VideoStreamParser(private val client: AnichinClient) {

    companion object {
        private const val TAG = "VideoStreamParser"

        // Patterns for extracting video URLs from various sources
        private val HLS_PATTERN = Pattern.compile("""https?://[^\s"']+\.m3u8[^\s"']*""", Pattern.CASE_INSENSITIVE)
        private val MP4_PATTERN = Pattern.compile("""https?://[^\s"']+\.mp4[^\s"']*""", Pattern.CASE_INSENSITIVE)
        private val IFRAME_PATTERN = Pattern.compile("""<iframe[^>]+src=["']([^"']+)["']""", Pattern.CASE_INSENSITIVE)
        private val SOURCE_PATTERN = Pattern.compile("""<source[^>]+src=["']([^"']+)["']""", Pattern.CASE_INSENSITIVE)
        private val EVAL_PATTERN = Pattern.compile("""eval\((.+?)\)""", Pattern.DOTALL)
        private val SCRIPT_PATTERN = Pattern.compile("""<script[^>]*>(.+?)</script>""", Pattern.DOTALL or Pattern.CASE_INSENSITIVE)

        // Known video host patterns
        private val HOST_PATTERNS = mapOf(
            "gdrive" to Pattern.compile("""https?://[^\s"']*drive\.google\.com[^\s"']*""", Pattern.CASE_INSENSITIVE),
            "mp4upload" to Pattern.compile("""https?://[^\s"']*mp4upload\.com[^\s"']*""", Pattern.CASE_INSENSITIVE),
            "streamtape" to Pattern.compile("""https?://[^\s"']*streamtape\.com[^\s"']*""", Pattern.CASE_INSENSITIVE),
            "mixdrop" to Pattern.compile("""https?://[^\s"']*mixdrop\.co[^\s"']*""", Pattern.CASE_INSENSITIVE),
            "streamwish" to Pattern.compile("""https?://[^\s"']*streamwish\.to[^\s"']*""", Pattern.CASE_INSENSITIVE),
            "filemoon" to Pattern.compile("""https?://[^\s"']*filemoon\.sx[^\s"']*""", Pattern.CASE_INSENSITIVE),
        )
    }

    /**
     * Parse video streams from an episode page document.
     */
    fun parseVideoStreams(document: Document, pageUrl: String): List<VideoUrl> {
        val streams = mutableListOf<VideoUrl>()
        val html = document.html()

        // Method 1: Direct video sources in page
        streams.addAll(extractFromSourceTags(html))

        // Method 2: Embedded iframes
        streams.addAll(extractFromIframes(html, pageUrl))

        // Method 3: JavaScript-embedded URLs
        streams.addAll(extractFromJavaScript(html))

        // Method 4: Data attributes
        streams.addAll(extractFromDataAttributes(document))

        // Method 5: Known hosting patterns
        streams.addAll(extractFromKnownHosts(html))

        // Deduplicate by URL
        return streams.distinctBy { it.url }
    }

    /**
     * Parse server links from the page (multi-server support).
     */
    fun parseServerLinks(document: Document): List<Pair<String, String>> {
        val servers = mutableListOf<Pair<String, String>>()

        document.select(".server-item, .server-option, [data-server], .episodiotitle a, .player-modes a").forEach { element ->
            try {
                val serverName = element.text().trim()
                val serverUrl = element.attr("abs:href").ifEmpty {
                    element.attr("abs:data-src").ifEmpty {
                        element.attr("abs:data-url")
                    }
                }

                if (serverUrl.isNotEmpty() && serverName.isNotEmpty()) {
                    servers.add(serverName to serverUrl)
                }
            } catch (e: Exception) {
                Log.w(TAG, "Error parsing server link: ${e.message}")
            }
        }

        document.select("select.server-select option, select#player option").forEach { option ->
            try {
                val serverName = option.text().trim()
                val serverUrl = option.attr("abs:value").ifEmpty { option.attr("abs:data-src") }

                if (serverUrl.isNotEmpty() && serverUrl.startsWith("http")) {
                    servers.add(serverName to serverUrl)
                }
            } catch (e: Exception) {
                Log.w(TAG, "Error parsing server option: ${e.message}")
            }
        }

        return servers.distinctBy { it.second }
    }

    /**
     * Extract video URLs from <source> tags.
     */
    private fun extractFromSourceTags(html: String): List<VideoUrl> {
        val streams = mutableListOf<VideoUrl>()
        val matcher = SOURCE_PATTERN.matcher(html)

        while (matcher.find()) {
            val url = matcher.group(1) ?: continue
            streams.add(createVideoUrl(url, "Direct"))
        }

        return streams
    }

    /**
     * Extract video URLs from iframe embeds.
     */
    private fun extractFromIframes(html: String, pageUrl: String): List<VideoUrl> {
        val streams = mutableListOf<VideoUrl>()
        val matcher = IFRAME_PATTERN.matcher(html)

        while (matcher.find()) {
            val iframeUrl = matcher.group(1) ?: continue

            if (iframeUrl.startsWith("http") && !iframeUrl.contains("about:blank")) {
                try {
                    val iframeDoc = client.getDocument(iframeUrl)
                    val iframeStreams = parseVideoStreams(iframeDoc, iframeUrl)
                    streams.addAll(iframeStreams)
                } catch (e: Exception) {
                    Log.w(TAG, "Error fetching iframe $iframeUrl: ${e.message}")
                }
            }
        }

        return streams
    }

    /**
     * Extract video URLs from JavaScript code.
     */
    private fun extractFromJavaScript(html: String): List<VideoUrl> {
        val streams = mutableListOf<VideoUrl>()

        // Check script tags for video URLs
        val scriptMatcher = SCRIPT_PATTERN.matcher(html)

        while (scriptMatcher.find()) {
            val scriptContent = scriptMatcher.group(1) ?: continue
            streams.addAll(extractUrlsFromText(scriptContent))
        }

        return streams
    }

    /**
     * Extract video URLs from data attributes.
     */
    private fun extractFromDataAttributes(document: Document): List<VideoUrl> {
        val streams = mutableListOf<VideoUrl>()

        document.select("[data-src], [data-url], [data-video], [data-file]").forEach { element ->
            val url = element.attr("abs:data-src").ifEmpty {
                element.attr("abs:data-url").ifEmpty {
                    element.attr("abs:data-video").ifEmpty {
                        element.attr("abs:data-file")
                    }
                }
            }

            if (url.startsWith("http") && isVideoUrl(url)) {
                streams.add(createVideoUrl(url, "Data Attribute"))
            }
        }

        return streams
    }

    /**
     * Extract video URLs from known hosting patterns.
     */
    private fun extractFromKnownHosts(html: String): List<VideoUrl> {
        val streams = mutableListOf<VideoUrl>()

        for ((hostName, pattern) in HOST_PATTERNS) {
            val matcher = pattern.matcher(html)
            while (matcher.find()) {
                val url = matcher.group(0) ?: continue
                streams.add(createVideoUrl(url, hostName.replaceFirstChar { it.uppercase() }))
            }
        }

        return streams
    }

    /**
     * Extract video URLs from arbitrary text.
     */
    private fun extractUrlsFromText(text: String): List<VideoUrl> {
        val streams = mutableListOf<VideoUrl>()

        // HLS
        val hlsMatcher = HLS_PATTERN.matcher(text)
        while (hlsMatcher.find()) {
            val url = hlsMatcher.group(0) ?: continue
            streams.add(VideoUrl(
                url = url,
                quality = "HLS",
                isHls = true,
            ))
        }

        // MP4
        val mp4Matcher = MP4_PATTERN.matcher(text)
        while (mp4Matcher.find()) {
            val url = mp4Matcher.group(0) ?: continue
            streams.add(VideoUrl(
                url = url,
                quality = detectQuality(url),
                isHls = false,
            ))
        }

        return streams
    }

    /**
     * Create a VideoUrl with quality detection.
     */
    private fun createVideoUrl(url: String, server: String): VideoUrl {
        val isHls = url.contains(".m3u8", ignoreCase = true)
        val quality = detectQuality(url)

        return VideoUrl(
            url = url,
            quality = if (isHls) "HLS ($server)" else "$quality ($server)",
            isHls = isHls,
            headers = mapOf(
                "User-Agent" to AnichinClient.USER_AGENT,
                "Referer" to AnichinClient.BASE_URL,
            ),
        )
    }

    /**
     * Detect video quality from URL or filename.
     */
    private fun detectQuality(url: String): String {
        val lower = url.lowercase()
        return when {
            "2160" in lower || "4k" in lower || "uhd" in lower -> "2160p"
            "1440" in lower || "2k" in lower || "qhd" in lower -> "1440p"
            "1080" in lower || "fhd" in lower -> "1080p"
            "720" in lower || "hd" in lower -> "720p"
            "480" in lower || "sd" in lower -> "480p"
            "360" in lower -> "360p"
            else -> "Unknown"
        }
    }

    /**
     * Check if a URL looks like a video URL.
     */
    private fun isVideoUrl(url: String): Boolean {
        val lower = url.lowercase()
        return lower.contains(".m3u8") ||
                lower.contains(".mp4") ||
                lower.contains(".mkv") ||
                lower.contains(".webm") ||
                lower.contains("/embed/") ||
                lower.contains("/player/") ||
                lower.contains("video")
    }
}
