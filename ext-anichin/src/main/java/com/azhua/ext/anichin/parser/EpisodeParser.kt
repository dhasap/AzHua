package com.azhua.ext.anichin.parser

import android.util.Log
import com.azhua.extension.api.model.EpisodePage
import com.azhua.ext.anichin.network.AnichinClient
import org.jsoup.nodes.Document
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.regex.Pattern

/**
 * Parser for episode lists on Anichin.
 */
class EpisodeParser(private val client: AnichinClient) {

    companion object {
        private const val TAG = "EpisodeParser"
        private val EPISODE_NUMBER_PATTERN = Pattern.compile("""(?:episode|ep\.?\s*)(\d+(?:\.\d+)?)""", Pattern.CASE_INSENSITIVE)
        private val NUMBER_PATTERN = Pattern.compile("""(\d+(?:\.\d+)?)""")
    }

    /**
     * Parse episode list from an anime detail page.
     */
    fun parseEpisodeList(document: Document, animeUrl: String): List<EpisodePage> {
        val episodes = mutableListOf<EpisodePage>()

        val selectors = listOf(
            ".episodelist li",
            ".episode-item",
            ".ep-item",
            ".episodes li",
            ".anime-episode-list li",
            ".eplister li",
            ".episodio",
        )

        for (selector in selectors) {
            val elements = document.select(selector)
            if (elements.isNotEmpty()) {
                for (element in elements) {
                    try {
                        val title = element.selectFirst("a, .ep-title, .title")?.text()?.trim()
                        val url = element.selectFirst("a")?.attr("abs:href")
                        val episodeNumber = parseEpisodeNumber(title ?: "")

                        if (!title.isNullOrEmpty() && !url.isNullOrEmpty()) {
                            episodes.add(EpisodePage(
                                url = url,
                                name = title,
                                episodeNumber = episodeNumber,
                                dateUpload = parseDate(element.selectFirst(".date, .release-date, .time")?.text()),
                            ))
                        }
                    } catch (e: Exception) {
                        Log.w(TAG, "Error parsing episode: ${e.message}")
                    }
                }
                break
            }
        }

        return episodes.sortedBy { it.episodeNumber }
    }

    /**
     * Extract episode number from title string.
     */
    private fun parseEpisodeNumber(title: String): Float {
        val matcher = EPISODE_NUMBER_PATTERN.matcher(title)
        return if (matcher.find()) {
            matcher.group(1)?.toFloatOrNull() ?: 0f
        } else {
            val numberMatcher = NUMBER_PATTERN.matcher(title)
            if (numberMatcher.find()) {
                numberMatcher.group(1)?.toFloatOrNull() ?: 0f
            } else {
                0f
            }
        }
    }

    /**
     * Parse date string to timestamp.
     */
    private fun parseDate(dateStr: String?): Long {
        if (dateStr.isNullOrEmpty()) return 0

        val formats = listOf(
            "yyyy-MM-dd",
            "dd/MM/yyyy",
            "dd-MM-yyyy",
            "MMMM dd, yyyy",
            "dd MMM yyyy",
        )

        for (format in formats) {
            try {
                val sdf = SimpleDateFormat(format, Locale.getDefault())
                return sdf.parse(dateStr)?.time ?: continue
            } catch (e: Exception) {
                continue
            }
        }

        return 0
    }
}
