package com.azhua.core.model

/**
 * Donghua with its latest watch progress info.
 */
data class DonghuaWithProgress(
    val donghua: Donghua,
    val lastEpisode: Episode?,
    val progress: Float,
    val lastWatchedAt: Long,
)
