package com.azhua.core.model

/**
 * Domain model for watch history entry.
 */
data class History(
    val id: Long = 0,
    val donghuaId: Long,
    val episodeId: Long,
    val watchAt: Long,
    val durationMs: Long = 0,
)
