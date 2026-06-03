package com.azhua.extension.api.model

data class VideoUrl(
    val url: String,
    val quality: String,
    val isHls: Boolean = false,
    val headers: Map<String, String> = emptyMap(),
    val subtitleTracks: List<SubtitleTrack> = emptyList(),
)

data class SubtitleTrack(
    val url: String,
    val language: String,
    val isDefault: Boolean = false,
)
