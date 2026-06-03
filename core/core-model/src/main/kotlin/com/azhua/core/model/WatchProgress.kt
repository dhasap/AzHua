package com.azhua.core.model

/**
 * Represents watch progress for a donghua (used in Continue Watching).
 */
data class WatchProgress(
    val donghua: Donghua,
    val episode: Episode,
    val lastWatchedAt: Long,
)
