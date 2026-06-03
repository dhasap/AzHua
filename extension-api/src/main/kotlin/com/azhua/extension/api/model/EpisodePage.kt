package com.azhua.extension.api.model

data class EpisodePage(
    val url: String,
    val name: String,
    val episodeNumber: Float,
    val dateUpload: Long,
    val thumbnailUrl: String? = null,
    val durationMs: Long = 0,
)
