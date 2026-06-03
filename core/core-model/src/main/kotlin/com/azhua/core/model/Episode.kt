package com.azhua.core.model

/**
 * Domain model for an Episode.
 */
data class Episode(
    val id: Long = 0,
    val donghuaId: Long,
    val sourceEpisodeId: String,
    val episodeNumber: Float,
    val title: String? = null,
    val thumbnailUrl: String? = null,
    val durationMs: Long = 0,
    val dateUpload: Long = 0,
    val lastWatchMs: Long = 0,
    val isWatched: Boolean = false,
    val isDownloaded: Boolean = false,
    val downloadPath: String? = null,
) {
    val watchProgress: Float
        get() = if (durationMs > 0) (lastWatchMs.toFloat() / durationMs).coerceIn(0f, 1f) else 0f

    val formattedDuration: String
        get() {
            val totalSeconds = durationMs / 1000
            val hours = totalSeconds / 3600
            val minutes = (totalSeconds % 3600) / 60
            val seconds = totalSeconds % 60
            return if (hours > 0) String.format("%d:%02d:%02d", hours, minutes, seconds)
            else String.format("%02d:%02d", minutes, seconds)
        }
}
