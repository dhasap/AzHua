package com.azhua.ext.anichin.parser

import android.util.Log
import com.azhua.extension.api.model.AnimeInfo
import com.azhua.extension.api.model.AnimeStatus
import com.azhua.ext.anichin.network.AnichinClient
import org.jsoup.nodes.Document

/**
 * Parser for anime detail pages on Anichin.
 */
class AnimeDetailParser(private val client: AnichinClient) {

    companion object {
        private const val TAG = "AnimeDetailParser"
    }

    /**
     * Parse anime details from a detail page document.
     */
    fun parseAnimeDetail(document: Document, url: String): AnimeInfo {
        val title = document.selectFirst("h1, .anime-title, .entry-title, .post-title")?.text()?.trim()
            ?: "Unknown Title"

        val cover = document.selectFirst(".anime-poster img, .poster img, .featured-image img, .thumb img")?.let {
            it.attr("abs:src").ifEmpty { it.attr("abs:data-src") }
        } ?: ""

        val synopsis = document.selectFirst(".anime-synopsis, .synopsis, .description, .entry-content, .storyline")?.text()?.trim()

        val genres = document.select(".genre a, .genres a, .genre-info a, a[rel=tag]").map { it.text().trim() }

        val status = parseStatus(document)

        val studio = document.selectFirst(".studio, .production, .info-label:contains(Studio) + .info-value, .spe span:contains(Studio)")?.text()?.trim()

        val year = document.selectFirst(".year, .release-year, .info-label:contains(Year) + .info-value")?.text()?.trim()?.toIntOrNull()

        val rating = document.selectFirst(".rating, .score, .info-label:contains(Rating) + .info-value")?.text()?.trim()?.toDoubleOrNull()

        return AnimeInfo(
            url = url,
            title = title,
            coverUrl = cover,
            synopsis = synopsis,
            genres = genres,
            status = status,
            studio = studio,
            year = year,
            rating = rating?.toFloat(),
        )
    }

    /**
     * Parse anime status from document.
     */
    private fun parseStatus(document: Document): AnimeStatus {
        val statusText = document.selectFirst(".status, .anime-status, .info-label:contains(Status) + .info-value, .spe span:contains(Status)")?.text()?.trim()?.lowercase()
            ?: return AnimeStatus.UNKNOWN

        return when {
            statusText.contains("ongoing") || statusText.contains("tayang") || statusText.contains("airing") -> AnimeStatus.ONGOING
            statusText.contains("completed") || statusText.contains("selesai") || statusText.contains("finished") -> AnimeStatus.COMPLETED
            statusText.contains("upcoming") || statusText.contains("akan datang") -> AnimeStatus.UNKNOWN
            statusText.contains("hiatus") -> AnimeStatus.HIATUS
            statusText.contains("cancelled") || statusText.contains("dibatalkan") -> AnimeStatus.CANCELLED
            else -> AnimeStatus.UNKNOWN
        }
    }
}
