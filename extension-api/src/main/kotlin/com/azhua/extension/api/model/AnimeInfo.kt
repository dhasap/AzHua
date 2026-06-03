package com.azhua.extension.api.model

data class AnimeInfo(
    val url: String,
    val title: String,
    val coverUrl: String,
    val synopsis: String? = null,
    val genres: List<String> = emptyList(),
    val status: AnimeStatus = AnimeStatus.UNKNOWN,
    val studio: String? = null,
    val year: Int? = null,
    val rating: Float? = null,
)

enum class AnimeStatus { ONGOING, COMPLETED, HIATUS, CANCELLED, UNKNOWN }
