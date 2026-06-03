package com.azhua.ext.anichin.parser

import android.util.Log
import com.azhua.extension.api.model.AnimeInfo
import com.azhua.extension.api.model.AnimePage
import com.azhua.extension.api.model.AnimeStatus
import com.azhua.ext.anichin.network.AnichinClient
import org.jsoup.nodes.Document

/**
 * Parser for anime list pages on Anichin.
 */
class AnimeListParser(private val client: AnichinClient) {

    companion object {
        private const val TAG = "AnimeListParser"
    }

    /**
     * Parse anime list from a page document.
     */
    fun parseAnimeList(document: Document): AnimePage {
        val animes = mutableListOf<AnimeInfo>()

        // Try multiple selectors for anime items
        val selectors = listOf(
            ".film-poster",
            ".anime-card",
            ".post-item",
            ".animpost",
            ".bs",
            "article",
            ".item",
        )

        for (selector in selectors) {
            val elements = document.select(selector)
            if (elements.isNotEmpty()) {
                for (element in elements) {
                    try {
                        val title = element.selectFirst(".film-name, .anime-title, .post-title, h2, h3, .title")?.text()?.trim()
                        val url = element.selectFirst("a")?.attr("abs:href")
                        val cover = element.selectFirst("img")?.let {
                            it.attr("abs:src").ifEmpty { it.attr("abs:data-src") }
                        }

                        if (!title.isNullOrEmpty() && !url.isNullOrEmpty()) {
                            animes.add(AnimeInfo(
                                url = url,
                                title = title,
                                coverUrl = cover ?: "",
                            ))
                        }
                    } catch (e: Exception) {
                        Log.w(TAG, "Error parsing anime item: ${e.message}")
                    }
                }
                break
            }
        }

        // Check for next page
        val hasNextPage = document.selectFirst(".next, .nav-next, a[rel=next]") != null

        return AnimePage(
            animes = animes,
            hasNextPage = hasNextPage,
            currentPage = 1,
        )
    }

    /**
     * Parse search results from a page document.
     */
    fun parseSearchResults(document: Document): AnimePage {
        return parseAnimeList(document)
    }
}
